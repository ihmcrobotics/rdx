package us.ihmc.rdx.imgui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gnu.trove.map.hash.TIntObjectHashMap;
import imgui.flag.ImGuiWindowFlags;
import imgui.ImGui;
import imgui.internal.ImGuiDockNode;
import imgui.type.ImBoolean;
import us.ihmc.log.LogTools;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ImGuiPanel extends ImGuiPanelSizeHandler
{
   private final String panelName;
   private Runnable render;
   private final ImBoolean isShowing;
   private final boolean hasMenuBar;
   private final TreeSet<ImGuiPanel> children = new TreeSet<>(Comparator.comparing(ImGuiPanel::getPanelName));
   private final ConcurrentLinkedQueue<ImGuiPanel> removalQueue = new ConcurrentLinkedQueue<>();
   private final ConcurrentLinkedQueue<ImGuiPanel> additionQueue = new ConcurrentLinkedQueue<>();

   @Nullable
   private ImGuiDockspacePanel parentDockspacePanel = null;
   private boolean isOnMainViewport = false;

   public ImGuiPanel(String panelName)
   {
      this(panelName, null, false);
   }

   public ImGuiPanel(String panelName, Runnable render)
   {
      this(panelName, render, false);
   }

   public ImGuiPanel(String panelName, Runnable render, boolean isShowing)
   {
      this(panelName, render, isShowing, false);
   }

   public ImGuiPanel(String panelName, Runnable render, boolean isShowing, boolean hasMenuBar)
   {
      this.panelName = panelName;
      this.render = render;
      this.isShowing = new ImBoolean(isShowing);
      this.hasMenuBar = hasMenuBar;
   }

   /* package-private */ void renderMenuItem()
   {
      renderMenuItem("");
   }
   /* package-private */ void renderMenuItem(String indent)
   {
      ImGui.menuItem(indent + panelName, "", isShowing);

      for (ImGuiPanel child : children)
      {
         child.renderMenuItem(indent + "\t");
      }
   }

   /* package-private */ void renderPanelAndChildren(TIntObjectHashMap<ImGuiDockspacePanel> dockIDMap)
   {
      while (!removalQueue.isEmpty())
         children.remove(removalQueue.poll());
      while (!additionQueue.isEmpty())
         children.add(additionQueue.poll());

      // If the dockspace closes, we want to not render because otherwise the
      // docked stuff will lose its place. Also, let's show the docked windows
      // when if it shows up again.
      if (parentDockspacePanel != null)
      {
         isShowing.set(parentDockspacePanel.getIsShowing());
      }

      if (isShowing.get() && render != null)
      {
         handleSizeBeforeBegin();

         // Keep regular panels from being able to create new viewports
         // Calling setNextWindowViewport essentially locks the next panel
         // to that viewport and it can't leave other than being dragged all
         // the way to a dockspace position on another dockspace window.
         if (isOnMainViewport)
         {
            ImGui.setNextWindowViewport(ImGui.getMainViewport().getID());
         }
         else if (parentDockspacePanel != null)
         {
            ImGui.setNextWindowViewport(parentDockspacePanel.getWindowViewport().getID());
         }

         int windowFlags = ImGuiWindowFlags.None;
         if (hasMenuBar)
            windowFlags |= ImGuiWindowFlags.MenuBar;
         ImGui.begin(panelName, isShowing, windowFlags);
         handleSizeAfterBegin();

         int windowDockID = ImGui.getWindowDockID();
         findParentDockspacePanel(windowDockID, dockIDMap);

         render.run();
         ImGui.end();
      }

      for (ImGuiPanel child : children)
      {
         child.renderPanelAndChildren(dockIDMap);
      }
   }

   private void findParentDockspacePanel(int nodeID, TIntObjectHashMap<ImGuiDockspacePanel> dockIDMap)
   {
      ImGuiDockNode dockNode = imgui.internal.ImGui.dockBuilderGetNode(nodeID);
      if (dockNode.ptr == 0) // Panel is not docked, floating, in the main viewport
      {
         parentDockspacePanel = null;
         isOnMainViewport = true;
      }
      else if (dockNode.isDockSpace())
      {
         parentDockspacePanel = dockIDMap.get(dockNode.getID());
         isOnMainViewport = parentDockspacePanel == null;
      }
      else
      {
         if (dockNode.getParentNode().ptr == 0)
            LogTools.error("Not sure why this would happen yet but pretty sure it did once.");
         else
            findParentDockspacePanel(dockNode.getParentNode().getID(), dockIDMap);
      }
   }

   public void addChild(ImGuiPanel child)
   {
      children.add(child);
   }

   public void queueRemoveChild(ImGuiPanel panel)
   {
      removalQueue.add(panel);

   }
   public void queueAddChild(ImGuiPanel panel)
   {
      additionQueue.add(panel);
   }

   /* package-private */ void load(Map.Entry<String, JsonNode> panelEntry)
   {
      if (panelName.equals(panelEntry.getKey()))
      {
         isShowing.set(panelEntry.getValue().asBoolean());
      }

      for (ImGuiPanel child : children)
      {
         child.load(panelEntry);
      }
   }

   /* package-private */ void save(ObjectNode anchorJSON)
   {
      anchorJSON.put(panelName, isShowing.get());

      for (ImGuiPanel child : children)
      {
         child.save(anchorJSON);
      }
   }

   public void setRenderMethod(Runnable render)
   {
      this.render = render;
   }

   public ImBoolean getIsShowing()
   {
      return isShowing;
   }

   public String getPanelName()
   {
      return panelName;
   }

   public TreeSet<ImGuiPanel> getChildren()
   {
      return children;
   }
}

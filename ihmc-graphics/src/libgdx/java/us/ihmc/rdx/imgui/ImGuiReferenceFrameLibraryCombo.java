package us.ihmc.rdx.imgui;

import imgui.ImGui;
import imgui.type.ImInt;
import us.ihmc.robotics.referenceFrames.ReferenceFrameLibrary;
import us.ihmc.robotics.referenceFrames.ReferenceFrameSupplier;

/**
 * Used to select between the reference frames in a library by human readable names.
 */
public class ImGuiReferenceFrameLibraryCombo
{
   private final ImGuiUniqueLabelMap labels = new ImGuiUniqueLabelMap(getClass());
   private final ImInt referenceFrameIndex = new ImInt();
   private final ReferenceFrameLibrary referenceFrameLibrary;

   public ImGuiReferenceFrameLibraryCombo(ReferenceFrameLibrary referenceFrameLibrary)
   {
      this.referenceFrameLibrary = referenceFrameLibrary;
   }

   public boolean render()
   {
      return ImGui.combo(labels.get("Reference frame"), referenceFrameIndex, referenceFrameLibrary.getReferenceFrameNames());
   }

   public boolean setSelectedReferenceFrame(String referenceFrameName)
   {
      int frameIndex = referenceFrameLibrary.findFrameIndexByName(referenceFrameName);
      boolean frameFound = frameIndex >= 0;
      if (frameFound)
      {
         referenceFrameIndex.set(frameIndex);
      }
      return frameFound;
   }

   public ReferenceFrameSupplier getSelectedReferenceFrame()
   {
      return referenceFrameLibrary.getReferenceFrameSuppliers().get(referenceFrameIndex.get());
   }
}

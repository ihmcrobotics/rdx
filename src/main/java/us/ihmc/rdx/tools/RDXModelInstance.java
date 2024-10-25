package us.ihmc.rdx.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.transform.interfaces.RigidBodyTransformReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.scs2.definition.visual.ColorDefinition;

public class RDXModelInstance extends ModelInstance
{
   public final Vector3 center = new Vector3();
   public final Vector3 dimensions = new Vector3();
   public float radius;

   private final static BoundingBox bounds = new BoundingBox();

   private final RigidBodyTransform tempTransform = new RigidBodyTransform();
   private float currentOpacity = Float.NaN;

   public RDXModelInstance(Model model)
   {
      super(model);

      convertMaterials();
   }

   public RDXModelInstance(ModelInstance modelInstance)
   {
      super(modelInstance);

      convertMaterials();
   }

   /** Ensures compatibility with the gdx-gltf PBR shader. */
   private void convertMaterials()
   {
      for (int i = 0; i < materials.size; i++)
      {
         Material material = materials.get(i);

         ColorAttribute diffuseColor = material.get(ColorAttribute.class, ColorAttribute.Diffuse);
         TextureAttribute diffuseTexture = material.get(TextureAttribute.class, TextureAttribute.Diffuse);

         boolean diffuseColorNeedsReplacement = diffuseColor != null && !(diffuseColor instanceof PBRColorAttribute);
         boolean diffuseTextureNeedsReplacement = diffuseTexture != null && !(diffuseTexture instanceof PBRTextureAttribute);

         if (diffuseColorNeedsReplacement || diffuseTextureNeedsReplacement)
         {
            material.clear();

            if (diffuseTextureNeedsReplacement)
            {
               material.set(PBRTextureAttribute.createBaseColorTexture(diffuseTexture.textureDescription.texture));
            }
            else if (diffuseColorNeedsReplacement)
            {
               material.set(PBRColorAttribute.createBaseColorFactor(diffuseColor.color));
            }
         }
      }
   }

   public void calculateBoundingBox()
   {
      calculateBoundingBox(bounds);
      bounds.getCenter(center);
      bounds.getDimensions(dimensions);
      radius = dimensions.len() / 2f;
   }

   public void setTransformToReferenceFrame(ReferenceFrame referenceFrame)
   {
      referenceFrame.getTransformToDesiredFrame(tempTransform, ReferenceFrame.getWorldFrame());
      LibGDXTools.toLibGDX(tempTransform, transform);
   }

   public void setTransformToWorldFrame(RigidBodyTransform transformToWorldFrame)
   {
      LibGDXTools.toLibGDX(transformToWorldFrame, transform);
   }

   public void setPoseInWorldFrame(RigidBodyTransformReadOnly pose)
   {
      LibGDXTools.toLibGDX(pose, tempTransform, transform);
   }

   public void setPositionInWorldFrame(Point3DReadOnly position)
   {
      LibGDXTools.toLibGDX(position, transform);
   }

   public void setOpacity(float opacity)
   {
      // Prevent allocating new BlendingMode over and over
      if (opacity != currentOpacity)
         LibGDXTools.setOpacity(this, opacity);
      currentOpacity = opacity;
   }

   public void setDiffuseColor(Color color)
   {
      LibGDXTools.setDiffuseColor(this, color);
   }

   public void setColor(ColorDefinition color)
   {
      Color colorGDX = LibGDXTools.toLibGDX(color);
      setDiffuseColor(colorGDX);
      if (colorGDX.a < 1.0f)
      {
         setOpacity(colorGDX.a);
      }
   }
}
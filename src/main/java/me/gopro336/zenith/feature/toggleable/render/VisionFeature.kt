package me.gopro336.zenith.feature.toggleable.render

import me.gopro336.zenith.feature.AnnotationHelper
import me.gopro336.zenith.feature.Category
import me.gopro336.zenith.feature.Feature
import me.gopro336.zenith.property.Property
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack

@AnnotationHelper(name = "Vision", description = "Makes things easy to see", category = Category.RENDER)
object VisionFeature: Feature() {

	var brightness = Property(this, "Brightness", "", true)
	var barriers = Property(this, "Barriers", "", true)

	val barrierModel: IBakedModel by lazy {
		mc.renderItem.itemModelMesher.getItemModel(ItemStack(Blocks.BARRIER))
	}

	override fun onEnable() {
		mc.renderGlobal?.loadRenderers()
	}

	override fun onDisable() {
		mc.renderGlobal?.loadRenderers()
	}
}

package me.gopro336.zenith.userInterface.screen.menu

import me.gopro336.zenith.Zenith
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiMainMenu

/**
 * @author Gopro336
 */
class ZenithMainMenu: GuiMainMenu() {
	override fun initGui() {
		super.initGui()
		val startHeight = height / 4 + 48
		val buttonHeight = 24
		this.buttonList.add(GuiButton(30, width / 2 - 100, startHeight + (buttonHeight * 5), "Zenith GUI"))
	}
	
	override fun actionPerformed(button: GuiButton) {
		if (button.id == 30) {
			val gui = Zenith.clickGUI
			//gui.setParent(this)
			mc.displayGuiScreen(gui)
			return
		}
		super.actionPerformed(button)
	}
}

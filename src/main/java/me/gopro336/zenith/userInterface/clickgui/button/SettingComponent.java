package me.gopro336.zenith.userInterface.clickgui.button;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.api.util.newRender.RenderUtils2D;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.feature.toggleable.Client.ClickGuiFeature;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.Property;

import java.awt.*;

/**
 * @author Gopro336
 * @since 11/21/2020
 */
public class SettingComponent implements IComponent {
	public Property<?> value;
	private Feature feature;
	private int posX;
	private int posY;
	private int width;
	public int height;
	public boolean hidden;
	public boolean extended = false;
	public boolean isSubSetting;
	private int subsCount = 0;

	public SettingComponent(Feature feature, int posX, int posY, int width, int height, boolean isSub) {
		this.feature = feature;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.isSubSetting = isSub;
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		//return false;
		ChatUtils.error("no homo");
	}

	@Override
	public void onRender(int mouseX, int mouseY, float partialTicks) {
	}

	@Override
	public void onKeyTyped(char character, int keyCode) {
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
	}

	@Override
	public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
	}

	@Override
	public void onUpdate(){
	}

	/** IGNORE THESE FOR NOW, THEY HAVE NOTHING TO DO WITH THE SETTING COMP */
	@Override
	public void setExtended(boolean extended) {
	}

	/** IGNORE THESE FOR NOW, THEY HAVE NOTHING TO DO WITH THE SETTING COMP */
	@Override
	public boolean isExtended() {
		return false;
	}

	public Property<?> getProperty(){
		return value;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public boolean isSubProperty(){
		return isSubSetting;
	}

	public SettingComponent getSelf() {
		return this;
	}

	public SettingComponent getType() {
		return this;
	}

	public void countSub(){
		subsCount++;
	}

	public int getSubCount(){
		return subsCount;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getYOffset(){
		return height;
	}

	public void setSettingExtended(boolean extended) {
		this.extended = extended;
	}

	public boolean isSettingExtended() {
		return extended;
	}

	public boolean isSubNotHidden(){
		if (!getProperty().getParentProperty().isVisible()) return false;
		if (!getProperty().getParentProperty().isExtended()) return false;
		return true;
	}

	public boolean isNotHidden() {
		return !(this.hidden);
	}

	public boolean isHidden() {
		return this.hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
		return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
	}

	protected boolean isWithinBuffer(final int mouseX, final int mouseY) {
		return (mouseX >= this.getPosX() && mouseX <= this.getPosX() + this.getWidth() && mouseY >= this.getPosY() && mouseY <= this.getPosY() + this.height);
	}

	public void preComponentRender(boolean drawButton){
		Zenith.clickGUI.drawGradient(posX, posY, posX + width, posY + height, new Color(20, 20, 20, ClickGuiFeature.backalpha.getValue()).getRGB(), new Color(20, 20, 20, ClickGuiFeature.backalpha.getValue()).getRGB());
		if (drawButton) drawButton();
	}

	public void drawButton(){
		Zenith.clickGUI.drawGradient((getPosX() + 1), (getPosY() + 0.5), (getPosX() + getWidth()) -1, (getPosY() + getHeight()) -0.5, getComponentColor(), getComponentColor());
	}

	public void postComponentRender(){
		if (isSubSetting) drawSubDropdownLine();
		if (ClickGuiFeature.dot.getValue() && getSubCount()>0) drawDropdownIndicator();
	}

	public void drawDropdownIndicator(){
		FontUtil.drawString("...", (float) ((getPosX() + getWidth() - 3) - FontUtil.getStringWidth("...")), (float) (getPosY() + 4), new Color(255, 255, 255, 255).getRGB());
	}

	public void drawSubDropdownLine(){
		RenderUtils2D.drawRect(getPosX()+2, getPosY(), getPosX()+3, getPosY()+ getHeight(), ClickGuiFeature.accentColor.getValue().getRGB());
	}
}

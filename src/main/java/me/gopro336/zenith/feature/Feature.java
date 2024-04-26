package me.gopro336.zenith.feature;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.IGlobals;
import me.gopro336.zenith.event.render.Render3DEvent;

public class Feature
	implements IGlobals, IToggleable {

	private final String name;
	private final String description;
	private Category category;

//	public static final Minecraft mc = Minecraft.getMinecraft();
	public boolean enabled;
	private boolean opened;
	private boolean visible = true;
	private boolean binding;
	public boolean isKeyDown;

	public float remainingAnimation = 0;

	private String featureMetadata = null;

	private AnnotationHelper getAnnotation() {
		if (getClass().isAnnotationPresent(AnnotationHelper.class)) {
			return getClass().getAnnotation(AnnotationHelper.class);
		}
		throw new IllegalStateException("Annotation '@AnnotationHelper' not found!");
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int key;// = getAnnotation().key();


	public int getKey() {
		return key;
	}

	/**
	 * Creates a new Feature. It's important that the given name
	 * does not contain any whitespaces and that no modules with the
	 * same name exist. A modules name is its unique identifier.
	 *
	 */
	public Feature() {
		this.name = getAnnotation().name();
		this.description = getAnnotation().description();
		this.category =  getAnnotation().category();
		this.key = this.getAnnotation().key();
	}

	public void onClientConnect() {}
	public void onClientDisconnect() {}

	public boolean nullCheck() {
		return mc.player == null || mc.world == null;
	}

	public void enable() {
		enabled = true;
		Zenith.INSTANCE.getEventManager().addEventListener(this);
		FeatureManager.toggledFeatures.add(this);
		onEnable();
	}

	public void disable() {
		enabled = false;
		Zenith.INSTANCE.getEventManager().removeEventListener(this);
		FeatureManager.toggledFeatures.remove(this);
		remainingAnimation = 0;
		onDisable();
	}

	public void toggle() {
		try {
			if (enabled) {
				disable();
			} else {
				enable();
			}
		} catch (Exception ignored) {
		}
	}

	public void setEnabled(boolean enabled) {
		if (enabled) {
			enable();
		} else {
			disable();
		}
	}

	public void onEnable() {
	}

	public void onDisable() {

	}

	public void onUpdate() {
	}

	public void onThread() {
	}

	public String getFeatureMetadata() {
		return featureMetadata;
	}

	public void setFeatureMetadata(String metaInfo) {
		this.featureMetadata = metaInfo;
	}

	public void onRender3D(Render3DEvent event) {
	}

	public String getDescription(){
		return description;
	}

	public boolean isNotHUD()
	{
		return category != Category.HUD;
	}

	public String getHudInfo() { return null; }

	public boolean hasProperties() {
		return Zenith.SettingManager.getPropertiesByMod(this).size() > 0;
	}

	public String getName()
	{
		return name;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

}
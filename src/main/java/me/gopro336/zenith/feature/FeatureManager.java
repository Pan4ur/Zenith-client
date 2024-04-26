package me.gopro336.zenith.feature;

import me.gopro336.zenith.event.render.Render3DEvent;
import me.gopro336.zenith.property.propertyrw.PropertyWR;
import me.gopro336.zenith.feature.hudElement.Element;
import me.gopro336.zenith.feature.toggleable.Client.ClickGuiFeature;
import me.gopro336.zenith.feature.toggleable.Combat.*;
import me.gopro336.zenith.feature.toggleable.Dummy;
import me.gopro336.zenith.feature.toggleable.exploit.AntiHunger;
import me.gopro336.zenith.feature.toggleable.exploit.OppSmoker;
import me.gopro336.zenith.feature.toggleable.exploit.PacketFly;
import me.gopro336.zenith.feature.hudElement.hudElement.*;
import me.gopro336.zenith.feature.toggleable.misc.*;
import me.gopro336.zenith.feature.toggleable.movement.*;
import me.gopro336.zenith.feature.toggleable.render.*;
import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.Wrapper;
import me.gopro336.zenith.property.Property;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A class with the purpose of initialising, storing,
 * and retrieving Features.
 */
public class FeatureManager {
	private static final ArrayList<Feature> FEATURES = new ArrayList<>();
	public static ArrayList<Feature> toggledFeatures = new ArrayList<>();

	private static final ArrayList<Element> HUD_ELEMENTS = new ArrayList<>();

	public static ArrayList<Feature> getToggledFeatures() {
		return toggledFeatures;
	}

	public void init() {
		Zenith.INSTANCE.getEventManager().removeEventListener(this);
		addSubscribe(new CustomFont());
		addSubscribe(new RainbowFeature());
		addSubscribe(new ClickGuiFeature());
		addSubscribe(new HUDEditor());
		addSubscribe(NoCompressionKick.INSTANCE);
		addSubscribe(new AutoFish());
		addSubscribe(new PacketFly());
		addSubscribe(new Safewalk());
		addSubscribe(new Sprint());
		addSubscribe(new AntiHunger());
		addSubscribe(new EntityControl());
		addSubscribe(new MobOwner());
		addSubscribe(new AntiSound());
		addSubscribe(new NewChunks());
		addSubscribe(new NoRender());
		addSubscribe(new ChamsRw());
		addSubscribe(new Swing());
		addSubscribe(new Blink());
		addSubscribe(new OppSmoker());
		addSubscribe(new RBandESP());
		addSubscribe(new Fullbright());
		addSubscribe(new Radar());
		addSubscribe(new NewChams());
		addSubscribe(new BoatFly());
		addSubscribe(new FastSwim());
		addSubscribe(new Dummy());
		addSubscribe(new Step());
		addSubscribe(new AntiSpam());
		addSubscribe(new HotbarRefill());
		addSubscribe(LazyItemSwitch.INSTANCE);
		addSubscribe(new Offhand());
		addSubscribe(new AutoLog());
		addSubscribe(new AutoReconnect());
		addSubscribe(new NoSlow());
		addSubscribe(new Burrow());
		addSubscribe(new Speed());
		addSubscribe(new HoleSnap());
		addSubscribe(new KillDisplay());
		addSubscribe(new Chat());
		addSubscribe(new HoleFill());
		addSubscribe(new RotationViewer());


		// HUD elements
		addSubscribeHud(new PingElement());
		addSubscribeHud(new TpsElement());
		addSubscribeHud(new CrystalsElement());
		addSubscribeHud(new TotemsElement());
		addSubscribeHud(new GapplesElement());
		addSubscribeHud(new WatermarkElement());
		addSubscribeHud(new CrystalViewerElement());
		addSubscribeHud(new CoordinatesElement());
		addSubscribeHud(new ActiveModulesElement());
		FEATURES.sort(FeatureManager::alphabetize);
	}

	/**
	 * Adds Feature to feature list
	 *
	 * @param feature the feature in question.
	 */
	private void addSubscribe(Feature feature) {
		FEATURES.add(feature);
	}

	/**
	 * Adds an Element type Feature to feature list,
	 * and hud element list.
	 *
	 * @param feature the feature in question.
	 */
	private void addSubscribeHud(Feature feature) {
		FEATURES.add(feature);
		HUD_ELEMENTS.add((Element) feature);
	}

	private static int alphabetize(Feature feature1, Feature feature2) {
		return feature1.getName().compareTo(feature2.getName());
	}

	public static void onUpdate() {
		//modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
		//Zenith.moduleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::onUpdate);
	}
	public static void onClientDisconnect() {
		Zenith.featureManager.getModules().stream().filter(Feature::isEnabled).forEach(Feature::onClientDisconnect);
	}
	public static void onClientConnect() {
		Zenith.featureManager.getModules().stream().filter(Feature::isEnabled).forEach(Feature::onClientConnect);
	}

	public void onRender3D(Render3DEvent event) {
		//this.modules.stream().filter(Module::isEnabled).forEach(module -> module.onRender3D(event));
		//Zenith.moduleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::onRender3D(event));
	}

	public static ArrayList<Element> getHudElements() {
		return HUD_ELEMENTS;
	}

	@Deprecated
	public Feature getModuleByName(String name) {
		return FEATURES.stream().filter(module -> module.getName()
				.equalsIgnoreCase(name))
				.findFirst()
				.orElse(null);
	}

	public static ArrayList<Feature> getModulesInCategory(Category category) {
		ArrayList<Feature> modulesInCategory = new ArrayList<>();
		for (Feature feature : FEATURES) {
			if (feature.getCategory().equals(category)) {
				modulesInCategory.add(feature);
			}
		}

		return modulesInCategory;
	}
	
	public ArrayList<Feature> getModules() {
		return FEATURES;
	}

	public static ArrayList<Feature> getModulesStatic() {
		return FEATURES;
	}

	public static ArrayList<Feature> getEnabledVisibleModules() {
		return (ArrayList<Feature>) FEATURES.stream().filter(Feature::isEnabled).filter(Feature::isVisible).collect(Collectors.toList());
	}

	public static Feature getFeatureByAlias(String s) {
		for (Feature feature : FEATURES){
			if (feature.getName().equalsIgnoreCase(s)) {
				return feature;
			}
			if (feature.getName().toLowerCase().startsWith(s.toLowerCase())) {
				return feature;
			}
		}
		return null;
	}

	public static PropertyWR<?> isPropertyInFeature(String s, String s1) {
		if (getFeatureByAlias(s) != null) {
			ArrayList<PropertyWR<?>> PropertiesFromFeature
					= getPropertiesFromFeature(Objects.requireNonNull(getFeatureByAlias(s)));

			for (PropertyWR<?> propertyWR : PropertiesFromFeature) {
				if (propertyWR.getName().equalsIgnoreCase(s1)) {
					return propertyWR;
				}
			}
		}

		return null;
	}

	public static Feature getFeatureByClass(Class<? extends Feature> clazz) {
		for (Feature Feature : FEATURES) {
			if (Feature.getClass() == clazz) return Feature;
		}
		return null;
	}


	@Deprecated
	public static Feature getModule(String name) {
		for (Feature feature : FEATURES) {
			if (feature.getName().equalsIgnoreCase(name)) return feature;
		}
		return null;
	}

	public static ArrayList<Feature> getModules(Category category) {
		ArrayList<Feature> modulesInCategory = new ArrayList<>();
		FEATURES.forEach(feature -> {
			if (feature.getCategory() == category) {
				modulesInCategory.add(feature);
			}
		});
		return modulesInCategory;
	}

	public static ArrayList<Feature> getEnabledModules() {
		ArrayList<Feature> enabledModules = new ArrayList<>();
		FEATURES.forEach(feature -> {
			if (feature.isEnabled()) {
				enabledModules.add(feature);
			}
		});

		return enabledModules;
	}

	public static ArrayList<PropertyWR<?>> getPropertiesFromFeature(Feature feature) {
		Feature cast = (Feature) feature.getClass().getSuperclass().cast(feature);
		ArrayList<PropertyWR<?>> list = new ArrayList<>();
		Field[] declaredFields = cast.getClass().getDeclaredFields();
		int length = declaredFields.length;

		Field field;
		for(int i = 0; i < length; ++i) {
			field = declaredFields[i];
			if (PropertyWR.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);

			}
		}

		declaredFields = cast.getClass().getSuperclass().getDeclaredFields();
		length = declaredFields.length;

		for(int i = 0; i < length; ++i) {
			field = declaredFields[i];
			if (PropertyWR.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);

				try {
					list.add((PropertyWR<?>)field.get(cast));
				} catch (IllegalAccessException var8) {
					var8.printStackTrace();
				}
			}
		}

		return list;
	}
	public static ArrayList<Property<?>> getOldPropertiesFromFeature(Feature feature) {
		Feature cast = (Feature) feature.getClass().getSuperclass().cast(feature);
		ArrayList<Property<?>> list = new ArrayList<>();
		Field[] declaredFields = cast.getClass().getDeclaredFields();
		int length = declaredFields.length;

		//Field field;
		for(Field field : declaredFields) {
		//for(int i = 0; i < length; ++i) {
			//field = declaredFields[i];
			if (PropertyWR.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);

			}
		}

		declaredFields = cast.getClass().getSuperclass().getDeclaredFields();

		for(Field field : declaredFields) {
//		for(int i = 0; i < length; ++i) {
			//field = declaredFields[i];
			if (Property.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);

				try {
					list.add((Property<?>)field.get(cast));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	/*public static ArrayList<Setting> getSettingList(Module inputModule) {
		Module module = (Module) inputModule.getClass().getSuperclass().cast(inputModule);
		ArrayList<Setting> settingList = new ArrayList<>();
		for (Field field : module.getClass().getDeclaredFields()) {
			if (Setting.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				try {
					if(ListenableSettingDecorator.class.isAssignableFrom(field.getType())) {
						settingList.add((ListenableSettingDecorator) field.get(module));
					} else {
						settingList.add((Setting) field.get(module));
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		for(Field field : module.getClass().getSuperclass().getDeclaredFields()) {
			if (Setting.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				try {
					settingList.add((Setting) field.get(module));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return settingList;
	}*/

	public static void keyListen() {
		if (Wrapper.mc.currentScreen == null) {
			for (Feature m : Zenith.featureManager.getModules()) {
				try {
					if (Keyboard.isKeyDown(Keyboard.KEY_NONE) || Keyboard.isKeyDown(Keyboard.CHAR_NONE))
						return;

					if (Keyboard.isKeyDown(m.getKey()) && !m.isKeyDown) {
						m.isKeyDown = true;
						m.toggle();
					}

					else if (!Keyboard.isKeyDown(m.getKey()))
						m.isKeyDown = false;

				} catch (Exception idc) {
					// idc.printStackTrace();
				}
			}
		}
	}
}

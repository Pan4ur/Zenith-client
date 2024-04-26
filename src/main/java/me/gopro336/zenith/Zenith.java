package me.gopro336.zenith;

import com.google.common.reflect.ClassPath;
import me.gopro336.zenith.api.util.newRotations.RotationManager;
import me.gopro336.zenith.api.util.time.TickRateUtil;
import me.gopro336.zenith.api.util.time.TimerManager;
import me.gopro336.zenith.api.config.ConfigManagerJSON;
import me.gopro336.zenith.core.Init;
import me.gopro336.zenith.core.InitStage;
import me.gopro336.zenith.event.EventProcessor;
import me.gopro336.zenith.event.TotemPopListener;
import me.gopro336.zenith.managment.*;
import me.gopro336.zenith.userInterface.clickgui.ClickGUI;
import me.gopro336.zenith.feature.FeatureManager;
import me.gopro336.zenith.feature.SettingManager;
import me.gopro336.zenith.api.util.IGlobals;
import me.gopro336.zenith.api.util.Logger;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import me.gopro336.zenith.userInterface.screen.HUDEditor;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;
import team.stiff.pomelo.impl.annotated.AnnotatedEventManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Zenith
 *
 * @author Gopro336
 * @author Danmaster2
 * @version 1.12.2
 * @since 11/x/2020
 */
@Mod(modid = "zenith", name = "Zenith", version = "1.4.0-beta")
public class Zenith implements IGlobals
{
	public static final String name = "Zenith";
	//public static final String version = "1.2.4-beta";
	public static final String version = "1.4.0-beta";
	public static final String creator = "Gopro336";

	@Mod.Instance
	public static Zenith 			INSTANCE;
	public static Logger 			logger;
	public static SettingManager 	SettingManager;// cant wait to get rid of this
	public static FeatureManager 	featureManager;
	public RotationManager 			rotationManager;
	public static TpsManager 		tpsManager;
	public static TickManager 		tickManager;
	public static ConfigManagerJSON configManagerJSON;
	public static CommandManager 	commandManager;
	public static TotemPopListener 	popListener;
	private AnnotatedEventManager 	eventManager = new AnnotatedEventManager();
	public ThreadManager 			threadManager;
	public static ClickGUI 			clickGUI;
	public static HUDEditor 		hudEditor;
	private static boolean 			unloaded;
	public TimerManager 			timerManager;

	@Mod.EventHandler
	public void init(FMLPreInitializationEvent event) throws IOException {
	//	init(InitStage.Pre);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) throws IOException {
		System.out.println("[ZENITH] initilize");
		Display.setTitle("Gopro is god");
		System.out.println("[ZENITH] Starting client, v2.3, created by Gopro336");
		SettingManager = new SettingManager();
		MinecraftForge.EVENT_BUS.register(new EventProcessor());
		System.out.println("EventManager loaded.");
//		init(InitStage.During);
		load();
	}

	@Mod.EventHandler
	public void init(FMLPostInitializationEvent event) {
		try {
			init(InitStage.Post);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		System.out.println("[ZENITH] Saved!");
	}

	//	@Init(stage = InitStage.Pre)
	public void msg(){
		System.out.println("[ZENITH] initilizeeeeee");
	}

	//	@Init(stage = InitStage.During)
	public void load() {
		logger = new Logger();

		threadManager = new ThreadManager();

		eventManager = new AnnotatedEventManager();

		featureManager = new FeatureManager();

		featureManager.init();

		rotationManager = new RotationManager();

		Zenith.INSTANCE.getEventManager().addEventListener(rotationManager);

		tpsManager = new TpsManager();

		popListener = new TotemPopListener();

		tickManager = new TickManager();

		configManagerJSON = new ConfigManagerJSON();

		commandManager = new CommandManager();

		clickGUI = new ClickGUI();

		hudEditor = new HUDEditor();

		timerManager = new TimerManager();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			ConfigManagerJSON.saveConfig();
		System.out.println("Saving Config!");
	}));

		ConfigManagerJSON.loadConfig();

		Zenith.INSTANCE.getEventManager().addEventListener(timerManager);

		TickRateUtil.INSTANCE = new TickRateUtil();

		System.out.println("[ZENITH] Loaded!");
	}

	public CommandManager getCommandManager() {
		return this.commandManager;
	}

	public AnnotatedEventManager getEventManager() {
		return this.eventManager;
	}

	public ThreadManager getThreadManager() {
		return threadManager;
	}

	/**
	 * Calls all methods annotated with @Init with
	 * regard to the InitStage parameter
	 *
	 * @param initStage the selected stage to call
	 */
	private static void init(InitStage initStage) throws IOException {

//		for (ClassPath.ClassInfo classInfo : ClassPath.from(Launch.classLoader).getAllClasses()) {
//			if(!classInfo.getName().startsWith("me.gopro336.zenith")) return;
//			Class clazz = classInfo.load();
//			for (Method method : clazz.getDeclaredMethods()){
//				if (method.isAnnotationPresent(Init.class)){
//
//				}
//			}
//		}

		//Set<Method> initTasks = getAnnotatedMethods().collect(Collectors.toSet());

//		Set<Method> initTasks = reflections.getMethodsAnnotatedWith(Init.class);
		//if (initTasks == null || initTasks.size() < 1) return;

	//	for (Method initTask : initTasks) {
		//	Init annotation = initTask.getAnnotation(Init.class);
		//	if (annotation != null && annotation.stage().equals(initStage)) reflectInit(initTask, annotation);
	//	}
	}

	/**
	 * Calls all methods annotated with @Init with
	 * regard to the InitStage parameter using reflections
	 *
	 * @param task the method to be called
	 * @param annotation the Init annotation
	 */
	private static void reflectInit(Method task, Init annotation) {
		Class<?>[] preTasks = annotation.dependencies();

		try {
			if (preTasks == null || preTasks.length < 1) {
				task.invoke(null);
			}
			else {
				for (Class<?> aClass : preTasks) {
//					Set<Method> preInits = new Reflections(aClass).getMethodsAnnotatedWith(Init.class);
					Set<Method> preInits = getMethods(aClass)
							.filter(method -> method.isAnnotationPresent(Init.class))
							.collect(Collectors.toSet());

					for (Method preInit : preInits) {
						Init preInitAnnotation = preInit.getAnnotation(Init.class);

						if (preInitAnnotation != null && preInitAnnotation.stage().equals(annotation.stage())) {
							reflectInit(preInit, preInitAnnotation);
						}
					}

					task.invoke(null);
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static Stream<Method> getAnnotatedMethods() throws IOException {
		List<Method> out = new ArrayList<>();
		//getClasses().forEach(clazz -> getMethods(clazz)
				//.filter(method -> method.isAnnotationPresent(Init.class))
				//.forEach(method -> out.add(method))
		//);
		return out.stream();
	}

	public static Stream<Method> getMethods(Class classFile) {
//		List<Method> out = new ArrayList<>();
		return Arrays.stream(classFile.getDeclaredMethods())
				.filter(method -> method.isAnnotationPresent(Init.class));
//				.forEach(method -> out.add(method));
//				;
//		return classFile.getMethods().stream().filter(MethodInfo::isMethod);
//		return out.stream();
	}

	public static Stream<Class> getClasses() throws IOException {
		List<Class> out = new ArrayList<>();
		//ClassPath.from(Launch.classLoader).getAllClasses().stream().forEach(
			//	classInfo -> out.add(classInfo.load())
		//);
		return out.stream();
	}
}
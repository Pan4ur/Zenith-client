package me.gopro336.zenith.managment;

import me.gopro336.zenith.api.util.IGlobals;
import me.gopro336.zenith.feature.FeatureManager;

public class ThreadManager implements IGlobals {
    public ThreadManager() {
        //super("ThreadManager", "Manages the main client service thread", 15);

        moduleService.setDaemon(true);
        moduleService.start();
    }

    // thread used by all modules
    ModuleService moduleService = new ModuleService();

    public static class ModuleService extends Thread implements IGlobals {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (!nullCheck()) {
                        yield();
                        continue;
                    }

                    FeatureManager.getToggledFeatures().forEach(module -> {
                        try {
                            module.onThread();
                        } catch (Exception ignored) {

                        }
                    });

                } catch (Exception ignored) {

                }
            }
        }
    }

    public ModuleService getService() {
        return moduleService;
    }
}

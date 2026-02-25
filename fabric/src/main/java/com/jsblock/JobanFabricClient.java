package com.jsblock;

import com.jsblock.client.JobanCustomResources;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

import static com.jsblock.client.JobanCustomResources.CUSTOM_RESOURCES_ID;

public class JobanFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		JobanClient.init();
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CustomResourcesWrapper());
	}

	private static class CustomResourcesWrapper implements SimpleSynchronousResourceReloadListener {

		@Override
		public ResourceLocation getFabricId() {
			return new ResourceLocation(Joban.MOD_ID, CUSTOM_RESOURCES_ID);
		}

		@Override
		public void onResourceManagerReload(ResourceManager resourceManager) {
			JobanCustomResources.reload(resourceManager);
		}
	}
}

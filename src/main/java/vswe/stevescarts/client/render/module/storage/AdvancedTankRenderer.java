package vswe.stevescarts.client.render.module.storage;

import vswe.stevescarts.client.render.module.ModuleRenderer;
import vswe.stevescarts.client.render.module.model.storage.AdvancedTankModel;
import vswe.stevescarts.client.render.module.storage.FluidRenderUtil;
import vswe.stevescarts.module.storage.TankModule;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class AdvancedTankRenderer extends ModuleRenderer<TankModule> {
	private final AdvancedTankModel model;

	public AdvancedTankRenderer(Identifier texture) {
		this.model = new AdvancedTankModel(texture);
	}

	@Override
	public void render(TankModule module, float entityYaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int entityLight) {
		matrices.push();
		matrices.scale(1.001F, 1.001F, 1.001F); // Fixes z-fighting
		this.model.render(matrices, vertexConsumers, entityLight, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
		FluidRenderUtil.renderFluidCuboid(matrices, module.getTank(), -8.0f, -7.0f, -6.0f, 16.0f, 14.0f, 12.0f);
	}
}

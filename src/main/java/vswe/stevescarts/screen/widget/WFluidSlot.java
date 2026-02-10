package vswe.stevescarts.screen.widget;

import vswe.stevescarts.util.TextHelper;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import vswe.stevescarts.client.render.RenderUtil;
import vswe.stevescarts.util.FluidUtils;
import vswe.stevescarts.util.Tank;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import vswe.stevescarts.StevesCarts;

public class WFluidSlot extends WWidget {
	public static final Texture TEXTURE = new Texture(StevesCarts.id("textures/gui/tank.png"), 0, 0, 1, 1);
	private final Tank tank;

	public WFluidSlot(Tank tank) {
		this.tank = tank;
		this.setSize(36, 51);
	}

	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		super.paint(matrices, x, y, mouseX, mouseY);
		// Utiliser notre nouvelle classe RenderUtil
		if (!this.tank.isEmpty()) {
			RenderUtil.renderGuiTank(this.tank.getFluid(), this.tank.getCapacity(), this.tank.getAmount(), x + 1, y + 1, 100.0, 34, 49);
		}

		ScreenDrawing.texturedRect(matrices, x, y, 36, 51, TEXTURE, 0xFFFFFFFF);
	}
	@Override
	public void addTooltip(TooltipBuilder tooltip) {
		tooltip.add(TextHelper.append(TextHelper.translatable("tooltip.stevescarts.fluid.max"), this.tank.getCapacity() + " mB"));
		tooltip.add(TextHelper.append(TextHelper.translatable("tooltip.stevescarts.fluid.current"), this.tank.getAmount() + " mB"));
		if (!this.tank.isEmpty()) {
			tooltip.add(TextHelper.formatted(
				TextHelper.append(TextHelper.translatable("tooltip.stevescarts.fluid.name"), FluidUtils.getFluidName(this.tank.getFluid())),
				Formatting.GRAY
			));
		}
	}
}

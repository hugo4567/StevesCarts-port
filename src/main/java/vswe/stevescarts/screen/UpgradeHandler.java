package vswe.stevescarts.screen;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WPlayerInvPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import vswe.stevescarts.block.entity.UpgradeBlockEntity;
import vswe.stevescarts.screen.widget.WFixedPanel;
import vswe.stevescarts.util.TextHelper;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

/**
 * Handler for the Upgrade GUI.
 * Allows players to configure upgrade modules for the cart assembler.
 */
public class UpgradeHandler extends SyncedGuiDescription {
    public static final int INVENTORY_SIZE = 4;
    
    private final UpgradeBlockEntity blockEntity;

    public UpgradeHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, UpgradeBlockEntity blockEntity) {
        super(StevesCartsScreenHandlers.UPGRADE, syncId, playerInventory, 
              getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));
        this.blockEntity = blockEntity;

        WFixedPanel root = new WFixedPanel();
        root.setSize(256, 190);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        // Title
        WLabel title = new WLabel(TextHelper.translatable("screen.stevescarts.upgrade.title"));
        title.setHorizontalAlignment(HorizontalAlignment.LEFT);
        root.add(title, 8, 6);

        // Upgrade slots (2x2 grid)
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            int x = i % 2;
            int y = i / 2;
            WItemSlot slot = WItemSlot.of(blockInventory, i);
            root.add(slot, 62 + x * 18, 30 + y * 18);
        }

        // Player inventory
        WPlayerInvPanel playerInvPanel = this.createPlayerInventoryPanel(false);
        root.add(playerInvPanel, (root.getWidth() - playerInvPanel.getWidth()) / 2 - root.getInsets().left(), 
                 root.getHeight() - playerInvPanel.getHeight() - 7);

        root.validate(this);
    }

    public UpgradeBlockEntity getBlockEntity() {
        return blockEntity;
    }
}

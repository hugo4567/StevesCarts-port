package vswe.stevescarts.screen;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WPlayerInvPanel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.block.entity.ActivatorBlockEntity;
import vswe.stevescarts.screen.widget.WFixedPanel;
import vswe.stevescarts.util.TextHelper;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for the Activator GUI (Cart Toggler).
 * Allows players to configure activation options for carts passing over the activator block.
 */
public class ActivatorHandler extends SyncedGuiDescription {
    private static final Identifier PACKET_TOGGLE_OPTION = StevesCarts.id("activator_toggle");
    
    private final ActivatorBlockEntity blockEntity;
    private final List<ActivatorOption> options;
    private final List<WToggleButton> toggleButtons = new ArrayList<>();

    public ActivatorHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ActivatorBlockEntity blockEntity) {
        super(StevesCartsScreenHandlers.ACTIVATOR, syncId, playerInventory, 
              getBlockInventory(context, 0), getBlockPropertyDelegate(context));
        this.blockEntity = blockEntity;
        this.options = createDefaultOptions();

        WFixedPanel root = new WFixedPanel();
        root.setSize(255, 222);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        // Title
        WLabel title = new WLabel(TextHelper.translatable("screen.stevescarts.activator.title"));
        title.setHorizontalAlignment(HorizontalAlignment.LEFT);
        root.add(title, 8, 6);

        // Option toggles
        for (int i = 0; i < options.size(); i++) {
            ActivatorOption option = options.get(i);
            int yPos = 22 + i * 22;
            
            WToggleButton toggle = new WToggleButton(TextHelper.translatable(option.getNameKey()));
            toggle.setToggle(option.isEnabled());
            final int optionIndex = i;
            toggle.setOnToggle(enabled -> {
                ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_TOGGLE_OPTION, buf -> {
                    buf.writeByte(optionIndex);
                    buf.writeBoolean(enabled);
                });
            });
            toggleButtons.add(toggle);
            root.add(toggle, 20, yPos);
        }

        // Player inventory
        WPlayerInvPanel playerInvPanel = this.createPlayerInventoryPanel(false);
        root.add(playerInvPanel, (root.getWidth() - playerInvPanel.getWidth()) / 2 - root.getInsets().left(), 
                 root.getHeight() - playerInvPanel.getHeight() - 7);

        // Network handler
        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_TOGGLE_OPTION, buf -> {
            int index = buf.readByte();
            boolean enabled = buf.readBoolean();
            if (index >= 0 && index < options.size()) {
                options.get(index).setEnabled(enabled);
                blockEntity.markDirty();
            }
        });

        root.validate(this);
    }

    private List<ActivatorOption> createDefaultOptions() {
        List<ActivatorOption> list = new ArrayList<>();
        list.add(new ActivatorOption("screen.stevescarts.activator.option.drill", "screen.stevescarts.activator.option.drill.info", true));
        list.add(new ActivatorOption("screen.stevescarts.activator.option.shield", "screen.stevescarts.activator.option.shield.info", true));
        list.add(new ActivatorOption("screen.stevescarts.activator.option.invisibility", "screen.stevescarts.activator.option.invisibility.info", true));
        list.add(new ActivatorOption("screen.stevescarts.activator.option.chunk", "screen.stevescarts.activator.option.chunk.info", true));
        list.add(new ActivatorOption("screen.stevescarts.activator.option.cage", "screen.stevescarts.activator.option.cage.info", true));
        return list;
    }

    public ActivatorBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public List<ActivatorOption> getOptions() {
        return options;
    }

    /**
     * Represents an activator option that can be toggled.
     */
    public static class ActivatorOption {
        private final String nameKey;
        private final String infoKey;
        private boolean enabled;

        public ActivatorOption(String nameKey, String infoKey, boolean defaultEnabled) {
            this.nameKey = nameKey;
            this.infoKey = infoKey;
            this.enabled = defaultEnabled;
        }

        public String getNameKey() { return nameKey; }
        public String getInfoKey() { return infoKey; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}

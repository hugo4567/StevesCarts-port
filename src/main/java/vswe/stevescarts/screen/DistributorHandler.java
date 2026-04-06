package vswe.stevescarts.screen;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WPlayerInvPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.block.entity.DistributorBlockEntity;
import vswe.stevescarts.screen.widget.WFixedPanel;
import vswe.stevescarts.util.TextHelper;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for the Distributor GUI.
 * Configures which manager sides should handle specific types of items/fluids.
 * Settings can be dragged and dropped onto different sides.
 */
public class DistributorHandler extends SyncedGuiDescription {
    private static final Identifier PACKET_ADD_SETTING = StevesCarts.id("distributor_add");
    private static final Identifier PACKET_REMOVE_SETTING = StevesCarts.id("distributor_remove");
    
    private final DistributorBlockEntity blockEntity;
    private final List<DistributorSide> sides = new ArrayList<>();
    private int activeSettingId = -1;

    public DistributorHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, DistributorBlockEntity blockEntity) {
        super(StevesCartsScreenHandlers.DISTRIBUTOR, syncId, playerInventory, 
              getBlockInventory(context, 0), getBlockPropertyDelegate(context));
        this.blockEntity = blockEntity;
        
        // Initialize sides (6 possible sides)
        for (int i = 0; i < 6; i++) {
            sides.add(new DistributorSide(i, getSideName(i)));
        }

        WFixedPanel root = new WFixedPanel();
        root.setSize(255, 186);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        // Title
        WLabel title = new WLabel(TextHelper.translatable("screen.stevescarts.distributor.title"));
        title.setHorizontalAlignment(HorizontalAlignment.LEFT);
        root.add(title, 8, 6);

        // Side boxes (showing which sides are enabled and their settings)
        int sideIndex = 0;
        for (DistributorSide side : sides) {
            if (side.isEnabled()) {
                int[] box = getSideBoxRect(sideIndex);
                
                // Side indicator button
                WButton sideButton = new WButton(TextHelper.literal(side.getName().substring(0, 1)));
                sideButton.setOnClick(() -> {
                    if (activeSettingId != -1) {
                        // Drop setting onto this side
                        ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_ADD_SETTING, buf -> {
                            buf.writeByte(activeSettingId);
                            buf.writeByte(side.getId());
                        });
                        activeSettingId = -1;
                    }
                });
                root.add(sideButton, box[0], box[1], box[2], box[3]);
                
                sideIndex++;
            }
        }

        // Available settings (bottom area)
        WLabel settingsLabel = new WLabel(TextHelper.translatable("screen.stevescarts.distributor.settings"));
        root.add(settingsLabel, 20, 130);

        // Add setting buttons (items to cargo, fluids to liquid, etc.)
        addSettingButton(root, 0, true, 20, 143, "Items (Top)");
        addSettingButton(root, 1, true, 38, 143, "Fluids (Top)");
        addSettingButton(root, 2, false, 20, 161, "Items (Bot)");
        addSettingButton(root, 3, false, 38, 161, "Fluids (Bot)");

        // Player inventory
        WPlayerInvPanel playerInvPanel = this.createPlayerInventoryPanel(false);
        root.add(playerInvPanel, (root.getWidth() - playerInvPanel.getWidth()) / 2 - root.getInsets().left(), 
                 root.getHeight() - playerInvPanel.getHeight() + 60);

        // Network handlers
        setupNetworkHandlers();

        root.validate(this);
    }

    private void addSettingButton(WFixedPanel root, int settingId, boolean isTop, int x, int y, String name) {
        WButton button = new WButton(TextHelper.literal(name.substring(0, 1)));
        button.setOnClick(() -> {
            activeSettingId = settingId;
        });
        root.add(button, x, y, 16, 16);
    }

    private int[] getSideBoxRect(int index) {
        return new int[] { 20, 18 + index * 24, 22, 22 };
    }

    private String getSideName(int id) {
        return switch (id) {
            case 0 -> "Down";
            case 1 -> "Up";
            case 2 -> "North";
            case 3 -> "South";
            case 4 -> "West";
            case 5 -> "East";
            default -> "Unknown";
        };
    }

    private void setupNetworkHandlers() {
        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_ADD_SETTING, buf -> {
            int settingId = buf.readByte();
            int sideId = buf.readByte();
            if (sideId >= 0 && sideId < sides.size()) {
                sides.get(sideId).addSetting(settingId);
                blockEntity.markDirty();
            }
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_REMOVE_SETTING, buf -> {
            int settingId = buf.readByte();
            int sideId = buf.readByte();
            if (sideId >= 0 && sideId < sides.size()) {
                sides.get(sideId).removeSetting(settingId);
                blockEntity.markDirty();
            }
        });
    }

    public DistributorBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public List<DistributorSide> getSides() {
        return sides;
    }

    /**
     * Represents a side of the distributor with its enabled settings.
     */
    public static class DistributorSide {
        private final int id;
        private final String name;
        private boolean enabled = true;
        private final List<Integer> settings = new ArrayList<>();

        public DistributorSide(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        public boolean hasSetting(int settingId) {
            return settings.contains(settingId);
        }
        
        public void addSetting(int settingId) {
            if (!settings.contains(settingId)) {
                settings.add(settingId);
            }
        }
        
        public void removeSetting(int settingId) {
            settings.remove(Integer.valueOf(settingId));
        }
        
        public List<Integer> getSettings() {
            return settings;
        }
    }
}

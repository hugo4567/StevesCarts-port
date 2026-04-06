package vswe.stevescarts.screen;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WPlayerInvPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.block.entity.CargoManagerBlockEntity;
import vswe.stevescarts.screen.widget.WFixedPanel;
import vswe.stevescarts.util.TextHelper;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

/**
 * Handler for the Cargo Manager GUI.
 * Manages item transfer between the manager's inventory and passing carts.
 * Features 4 transfer slots with directional arrows and color-coded sides.
 */
public class CargoHandler extends SyncedGuiDescription {
    private static final Identifier PACKET_TOGGLE_DIRECTION = StevesCarts.id("cargo_direction");
    private static final Identifier PACKET_CHANGE_COLOR = StevesCarts.id("cargo_color");
    private static final Identifier PACKET_CHANGE_AMOUNT = StevesCarts.id("cargo_amount");
    private static final Identifier PACKET_TOGGLE_RETURN = StevesCarts.id("cargo_return");
    private static final Identifier PACKET_CHANGE_LAYOUT = StevesCarts.id("cargo_layout");
    private static final Identifier PACKET_CHANGE_TARGET = StevesCarts.id("cargo_target");
    
    private static final Identifier TEXTURE = StevesCarts.id("textures/gui/cargoversion0part1.png");
    private static final int INVENTORY_SIZE = 27;
    
    private final CargoManagerBlockEntity blockEntity;
    
    // Transfer settings for 4 transfer slots
    private final boolean[] toCart = {true, true, false, false};
    private final int[] color = {1, 2, 3, 4}; // 1-4 = colors, 5 = disabled
    private final boolean[] doReturn = {false, false, false, false};
    private int layoutType = 0;
    private final int[] target = {0, 0, 0, 0}; // Target storage area
    private final int[] amount = {0, 0, 0, 0}; // Transfer amount
    private final int[] amountType = {0, 0, 0, 0}; // 0 = all, 1 = items, 2 = stacks

    public CargoHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CargoManagerBlockEntity blockEntity) {
        super(StevesCartsScreenHandlers.CARGO, syncId, playerInventory, 
              getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));
        this.blockEntity = blockEntity;

        WFixedPanel root = new WFixedPanel();
        root.setSize(305, 222);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        // Title - Manager name on left, "External Storage" on right
        WLabel titleLeft = new WLabel(TextHelper.translatable("screen.stevescarts.cargo.title"));
        titleLeft.setHorizontalAlignment(HorizontalAlignment.LEFT);
        root.add(titleLeft, 8, 6);
        
        WLabel titleRight = new WLabel(TextHelper.translatable("screen.stevescarts.manager.external_storage"));
        titleRight.setHorizontalAlignment(HorizontalAlignment.LEFT);
        root.add(titleRight, 160, 6);

        // Add transfer control widgets for each of the 4 transfer slots
        addTransferControls(root, 0, 8, 20);   // Top-left
        addTransferControls(root, 1, 158, 20);  // Top-right
        addTransferControls(root, 2, 8, 75);   // Bottom-left
        addTransferControls(root, 3, 158, 75);  // Bottom-right

        // Center layout toggle button
        WButton layoutButton = new WButton(TextHelper.translatable("screen.stevescarts.cargo.layout"));
        layoutButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_LAYOUT, buf -> {});
        });
        root.add(layoutButton, 125, 50, 50, 20);

        // Main inventory slots (3x9 grid for 27 slots)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                WItemSlot slot = WItemSlot.of(blockInventory, row * 9 + col);
                root.add(slot, 8 + col * 18, 130 + row * 18);
            }
        }

        // Player inventory
        WPlayerInvPanel playerInvPanel = this.createPlayerInventoryPanel(false);
        root.add(playerInvPanel, (root.getWidth() - playerInvPanel.getWidth()) / 2 - root.getInsets().left(), 
                 root.getHeight() - playerInvPanel.getHeight() - 7);

        // Network handlers
        setupNetworkHandlers();

        root.validate(this);
    }

    private void addTransferControls(WFixedPanel root, int id, int baseX, int baseY) {
        // Direction toggle button (arrow)
        WButton dirButton = new WButton(TextHelper.literal(toCart[id] ? "→" : "←"));
        dirButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_TOGGLE_DIRECTION, buf -> {
                buf.writeByte(id);
            });
        });
        root.add(dirButton, baseX + 40, baseY, 20, 20);

        // Color picker button
        WButton colorButton = new WButton(TextHelper.literal("C" + color[id]));
        colorButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_COLOR, buf -> {
                buf.writeByte(id);
                buf.writeBoolean(false); // left click = next color
            });
        });
        root.add(colorButton, baseX, baseY + 25, 20, 16);

        // Return toggle button
        WButton returnButton = new WButton(TextHelper.literal(doReturn[id] ? "R" : "-"));
        returnButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_TOGGLE_RETURN, buf -> {
                buf.writeByte(id);
            });
        });
        root.add(returnButton, baseX + 25, baseY + 25, 20, 16);

        // Amount control
        WButton amountButton = new WButton(TextHelper.literal(getAmountText(id)));
        amountButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_AMOUNT, buf -> {
                buf.writeByte(id);
                buf.writeBoolean(false);
            });
        });
        root.add(amountButton, baseX + 70, baseY, 40, 16);

        // Target storage area button
        WButton targetButton = new WButton(TextHelper.translatable("screen.stevescarts.cargo.target"));
        targetButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_TARGET, buf -> {
                buf.writeByte(id);
            });
        });
        root.add(targetButton, baseX, baseY, 35, 20);
    }

    private String getAmountText(int id) {
        if (amountType[id] == 0) {
            return "ALL";
        } else if (amountType[id] == 1) {
            return amount[id] + "I";
        } else {
            return amount[id] + "S";
        }
    }

    private void setupNetworkHandlers() {
        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_TOGGLE_DIRECTION, buf -> {
            int id = buf.readByte();
            if (id >= 0 && id < 4) {
                toCart[id] = !toCart[id];
                blockEntity.markDirty();
            }
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_CHANGE_COLOR, buf -> {
            int id = buf.readByte();
            boolean rightClick = buf.readBoolean();
            if (id >= 0 && id < 4) {
                color[id] = rightClick ? (color[id] == 1 ? 5 : color[id] - 1) : (color[id] == 5 ? 1 : color[id] + 1);
                blockEntity.markDirty();
            }
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_TOGGLE_RETURN, buf -> {
            int id = buf.readByte();
            if (id >= 0 && id < 4 && color[id] != 5) {
                doReturn[color[id] - 1] = !doReturn[color[id] - 1];
                blockEntity.markDirty();
            }
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_CHANGE_AMOUNT, buf -> {
            int id = buf.readByte();
            boolean rightClick = buf.readBoolean();
            if (id >= 0 && id < 4) {
                if (rightClick) {
                    // Decrease
                    if (amount[id] > 0) {
                        amount[id]--;
                    } else if (amountType[id] > 0) {
                        amountType[id]--;
                        amount[id] = 64;
                    }
                } else {
                    // Increase
                    if (amountType[id] == 0) {
                        amountType[id] = 1;
                        amount[id] = 1;
                    } else if (amount[id] < 64) {
                        amount[id]++;
                    } else if (amountType[id] < 2) {
                        amountType[id]++;
                        amount[id] = 1;
                    }
                }
                blockEntity.markDirty();
            }
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_CHANGE_LAYOUT, buf -> {
            layoutType = (layoutType + 1) % 3;
            blockEntity.markDirty();
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_CHANGE_TARGET, buf -> {
            int id = buf.readByte();
            if (id >= 0 && id < 4) {
                target[id] = (target[id] + 1) % 5; // Cycle through target options
                blockEntity.markDirty();
            }
        });
    }

    public CargoManagerBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public boolean isToCart(int id) { return toCart[id]; }
    public int getColor(int id) { return color[id]; }
    public boolean getDoReturn(int id) { return doReturn[id]; }
    public int getLayoutType() { return layoutType; }
    public int getTarget(int id) { return target[id]; }
    public int getAmount(int id) { return amount[id]; }
    public int getAmountType(int id) { return amountType[id]; }
}

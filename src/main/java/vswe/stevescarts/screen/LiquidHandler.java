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
import vswe.stevescarts.block.entity.LiquidManagerBlockEntity;
import vswe.stevescarts.screen.widget.WFixedPanel;
import vswe.stevescarts.screen.widget.WFluidSlot;
import vswe.stevescarts.util.TextHelper;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

/**
 * Handler for the Liquid Manager GUI.
 * Manages fluid transfer between the manager's tanks and passing carts.
 * Features 4 tank slots with directional arrows and color-coded sides.
 */
public class LiquidHandler extends SyncedGuiDescription {
    private static final Identifier PACKET_TOGGLE_DIRECTION = StevesCarts.id("liquid_direction");
    private static final Identifier PACKET_CHANGE_COLOR = StevesCarts.id("liquid_color");
    private static final Identifier PACKET_CHANGE_AMOUNT = StevesCarts.id("liquid_amount");
    private static final Identifier PACKET_TOGGLE_RETURN = StevesCarts.id("liquid_return");
    private static final Identifier PACKET_CHANGE_LAYOUT = StevesCarts.id("liquid_layout");
    
    private final LiquidManagerBlockEntity blockEntity;
    
    // Transfer settings for 4 tank slots
    private final boolean[] toCart = {true, true, false, false};
    private final int[] color = {1, 2, 3, 4}; // 1-4 = colors, 5 = disabled
    private final boolean[] doReturn = {false, false, false, false};
    private int layoutType = 0;
    private final int[] maxAmount = {0, 0, 0, 0}; // Max transfer in mB (0 = unlimited)
    private final boolean[] hasMaxAmount = {false, false, false, false};

    public LiquidHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, LiquidManagerBlockEntity blockEntity) {
        super(StevesCartsScreenHandlers.LIQUID, syncId, playerInventory, 
              getBlockInventory(context, 0), getBlockPropertyDelegate(context));
        this.blockEntity = blockEntity;

        WFixedPanel root = new WFixedPanel();
        root.setSize(230, 222);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        // Title
        WLabel titleLeft = new WLabel(TextHelper.translatable("screen.stevescarts.liquid.title"));
        titleLeft.setHorizontalAlignment(HorizontalAlignment.LEFT);
        root.add(titleLeft, 8, 6);
        
        WLabel titleRight = new WLabel(TextHelper.translatable("screen.stevescarts.manager.external_storage"));
        titleRight.setHorizontalAlignment(HorizontalAlignment.LEFT);
        root.add(titleRight, 130, 6);

        // Tank displays and controls for 4 tanks (2x2 layout)
        addTankControls(root, 0, 25, 12);   // Top-left
        addTankControls(root, 1, 169, 12);  // Top-right
        addTankControls(root, 2, 25, 75);   // Bottom-left
        addTankControls(root, 3, 169, 75);  // Bottom-right

        // Center layout toggle button
        WButton layoutButton = new WButton(TextHelper.translatable("screen.stevescarts.liquid.layout"));
        layoutButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_LAYOUT, buf -> {});
        });
        root.add(layoutButton, 90, 50, 50, 20);

        // Direction arrows between tanks (center area)
        addDirectionArrow(root, 0, 62, 20);
        addDirectionArrow(root, 1, 132, 20);
        addDirectionArrow(root, 2, 62, 83);
        addDirectionArrow(root, 3, 132, 83);

        // Player inventory
        WPlayerInvPanel playerInvPanel = this.createPlayerInventoryPanel(false);
        root.add(playerInvPanel, (root.getWidth() - playerInvPanel.getWidth()) / 2 - root.getInsets().left(), 
                 root.getHeight() - playerInvPanel.getHeight() - 7);

        // Network handlers
        setupNetworkHandlers();

        root.validate(this);
    }

    private void addTankControls(WFixedPanel root, int id, int baseX, int baseY) {
        // Tank display (placeholder - would need fluid rendering)
        WLabel tankLabel = new WLabel(TextHelper.literal("Tank " + (id + 1)));
        root.add(tankLabel, baseX, baseY);

        // Color picker button below tank
        WButton colorButton = new WButton(TextHelper.literal("C" + color[id]));
        colorButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_COLOR, buf -> {
                buf.writeByte(id);
                buf.writeBoolean(false);
            });
        });
        root.add(colorButton, baseX, baseY + 52, 16, 16);

        // Return toggle
        WButton returnButton = new WButton(TextHelper.literal(doReturn[id] ? "R" : "-"));
        returnButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_TOGGLE_RETURN, buf -> {
                buf.writeByte(id);
            });
        });
        root.add(returnButton, baseX + 18, baseY + 52, 16, 16);
    }

    private void addDirectionArrow(WFixedPanel root, int id, int x, int y) {
        WButton arrowButton = new WButton(TextHelper.literal(toCart[id] ? "→" : "←"));
        arrowButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_TOGGLE_DIRECTION, buf -> {
                buf.writeByte(id);
            });
        });
        root.add(arrowButton, x, y, 28, 28);

        // Amount label below arrow
        WButton amountButton = new WButton(TextHelper.literal(getAmountText(id)));
        amountButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_AMOUNT, buf -> {
                buf.writeByte(id);
                buf.writeBoolean(false);
            });
        });
        root.add(amountButton, x, y + 30, 28, 12);
    }

    private String getAmountText(int id) {
        if (!hasMaxAmount[id]) {
            return "ALL";
        }
        float buckets = maxAmount[id] / 1000.0f;
        String s = String.valueOf(buckets);
        if (s.endsWith(".0")) {
            s = s.substring(0, s.length() - 2);
        } else if (s.startsWith("0")) {
            s = s.substring(1);
        }
        return s + "B";
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
                    if (hasMaxAmount[id] && maxAmount[id] > 500) {
                        maxAmount[id] -= 500;
                    } else {
                        hasMaxAmount[id] = false;
                        maxAmount[id] = 0;
                    }
                } else {
                    // Increase
                    if (!hasMaxAmount[id]) {
                        hasMaxAmount[id] = true;
                        maxAmount[id] = 500;
                    } else if (maxAmount[id] < 16000) {
                        maxAmount[id] += 500;
                    }
                }
                blockEntity.markDirty();
            }
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_CHANGE_LAYOUT, buf -> {
            layoutType = (layoutType + 1) % 3;
            blockEntity.markDirty();
        });
    }

    public LiquidManagerBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public boolean isToCart(int id) { return toCart[id]; }
    public int getColor(int id) { return color[id]; }
    public boolean getDoReturn(int id) { return doReturn[id]; }
    public int getLayoutType() { return layoutType; }
    public int getMaxAmount(int id) { return maxAmount[id]; }
    public boolean hasMaxAmount(int id) { return hasMaxAmount[id]; }
}

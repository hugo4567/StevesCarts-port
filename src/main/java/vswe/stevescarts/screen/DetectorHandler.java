package vswe.stevescarts.screen;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WPlayerInvPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.block.entity.DetectorBlockEntity;
import vswe.stevescarts.block.entity.DetectorBlockEntity.DetectorType;
import vswe.stevescarts.screen.widget.WFixedPanel;
import vswe.stevescarts.util.TextHelper;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for the Detector GUI.
 * Allows configuring complex logic trees for cart detection.
 * Features dropdown menus for modules, states, and operators.
 */
public class DetectorHandler extends SyncedGuiDescription {
    private static final Identifier PACKET_ADD_LOGIC = StevesCarts.id("detector_add");
    private static final Identifier PACKET_REMOVE_LOGIC = StevesCarts.id("detector_remove");
    private static final Identifier PACKET_MOVE_LOGIC = StevesCarts.id("detector_move");
    private static final Identifier PACKET_CHANGE_TYPE = StevesCarts.id("detector_type");
    private static final Identifier PACKET_CHANGE_RANGE = StevesCarts.id("detector_range");
    
    private final DetectorBlockEntity blockEntity;
    private final LogicObject mainLogic;
    private LogicObject currentDragging = null;

    // Dropdown menu states
    private boolean modulesMenuOpen = false;
    private boolean statesMenuOpen = false;
    private boolean operatorsMenuOpen = false;

    public DetectorHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, DetectorBlockEntity blockEntity) {
        super(StevesCartsScreenHandlers.DETECTOR, syncId, playerInventory, 
              getBlockInventory(context, 0), getBlockPropertyDelegate(context));
        this.blockEntity = blockEntity;
        this.mainLogic = new LogicObject(LogicType.OUTPUT, (byte) 0);

        WFixedPanel root = new WFixedPanel();
        root.setSize(255, 202);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        // Title - Detector type name
        WLabel title = new WLabel(TextHelper.translatable("screen.stevescarts.detector.title"));
        title.setHorizontalAlignment(HorizontalAlignment.LEFT);
        root.add(title, 8, 6);

        // Detector type selector
        WLabel typeLabel = new WLabel(TextHelper.translatable("screen.stevescarts.detector.type"));
        root.add(typeLabel, 8, 24);
        
        WButton typeButton = new WButton(TextHelper.literal(blockEntity.getDetectorType().displayName));
        typeButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_TYPE, buf -> {});
        });
        root.add(typeButton, 60, 20, 60, 20);

        // Detection range control
        WLabel rangeLabel = new WLabel(TextHelper.translatable("screen.stevescarts.detector.range"));
        root.add(rangeLabel, 130, 24);
        
        WButton rangeDownButton = new WButton(TextHelper.literal("-"));
        rangeDownButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_RANGE, buf -> {
                buf.writeBoolean(false);
            });
        });
        root.add(rangeDownButton, 180, 20, 16, 20);
        
        WLabel rangeValue = new WLabel(TextHelper.literal(String.valueOf(blockEntity.getDetectionRange())));
        root.add(rangeValue, 200, 24);
        
        WButton rangeUpButton = new WButton(TextHelper.literal("+"));
        rangeUpButton.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_RANGE, buf -> {
                buf.writeBoolean(true);
            });
        });
        root.add(rangeUpButton, 220, 20, 16, 20);

        // Logic tree display area
        WPlainPanel logicArea = new WPlainPanel();
        logicArea.setSize(235, 100);
        root.add(logicArea, 10, 45);

        // Main output node
        WButton outputNode = new WButton(TextHelper.literal("OUTPUT"));
        outputNode.setOnClick(() -> {
            if (currentDragging != null) {
                // Drop logic onto output
                mainLogic.addChild(currentDragging);
                currentDragging = null;
            }
        });
        logicArea.add(outputNode, 100, 10, 50, 16);

        // Dropdown menus for available logic elements
        addDropdownMenu(root, "Modules", 10, 150, this::toggleModulesMenu);
        addDropdownMenu(root, "States", 90, 150, this::toggleStatesMenu);
        addDropdownMenu(root, "Operators", 170, 150, this::toggleOperatorsMenu);

        // Player inventory is not shown for detector (no slots)
        // But we keep a minimal footer

        // Network handlers
        setupNetworkHandlers();

        root.validate(this);
    }

    private void addDropdownMenu(WFixedPanel root, String name, int x, int y, Runnable onClick) {
        WButton menuButton = new WButton(TextHelper.literal(name));
        menuButton.setOnClick(onClick);
        root.add(menuButton, x, y, 70, 18);
    }

    private void toggleModulesMenu() {
        modulesMenuOpen = !modulesMenuOpen;
        statesMenuOpen = false;
        operatorsMenuOpen = false;
    }

    private void toggleStatesMenu() {
        statesMenuOpen = !statesMenuOpen;
        modulesMenuOpen = false;
        operatorsMenuOpen = false;
    }

    private void toggleOperatorsMenu() {
        operatorsMenuOpen = !operatorsMenuOpen;
        modulesMenuOpen = false;
        statesMenuOpen = false;
    }

    private void setupNetworkHandlers() {
        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_ADD_LOGIC, buf -> {
            byte type = buf.readByte();
            byte data = buf.readByte();
            // Add logic to tree
            blockEntity.markDirty();
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_REMOVE_LOGIC, buf -> {
            int nodeId = buf.readInt();
            // Remove logic from tree
            blockEntity.markDirty();
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_MOVE_LOGIC, buf -> {
            int nodeId = buf.readInt();
            int targetId = buf.readInt();
            // Move logic in tree
            blockEntity.markDirty();
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_CHANGE_TYPE, buf -> {
            DetectorType[] types = DetectorType.values();
            int currentOrdinal = blockEntity.getDetectorType().ordinal();
            blockEntity.setDetectorType(types[(currentOrdinal + 1) % types.length]);
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_CHANGE_RANGE, buf -> {
            boolean increase = buf.readBoolean();
            int currentRange = blockEntity.getDetectionRange();
            if (increase && currentRange < 32) {
                blockEntity.setDetectionRange(currentRange + 1);
            } else if (!increase && currentRange > 1) {
                blockEntity.setDetectionRange(currentRange - 1);
            }
        });
    }

    public DetectorBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public LogicObject getMainLogic() {
        return mainLogic;
    }

    /**
     * Types of logic nodes in the detector tree.
     */
    public enum LogicType {
        MODULE,   // Cart module detector
        STATE,    // Module state check
        OPERATOR, // Logic operator (AND, OR, NOT, etc.)
        OUTPUT    // Root output node
    }

    /**
     * Represents a logic node in the detector's logic tree.
     */
    public static class LogicObject {
        private final LogicType type;
        private final byte data; // Module ID, State ID, or Operator ID
        private LogicObject parent;
        private final List<LogicObject> children = new ArrayList<>();
        private int[] rect; // Screen position

        public LogicObject(LogicType type, byte data) {
            this.type = type;
            this.data = data;
        }

        public LogicType getType() { return type; }
        public byte getData() { return data; }
        public LogicObject getParent() { return parent; }
        public void setParent(LogicObject parent) { this.parent = parent; }
        public List<LogicObject> getChildren() { return children; }
        
        public void addChild(LogicObject child) {
            if (hasRoomForChild() && isChildValid(child)) {
                children.add(child);
                child.setParent(this);
            }
        }
        
        public void removeChild(LogicObject child) {
            children.remove(child);
            child.setParent(null);
        }

        public boolean hasRoomForChild() {
            return switch (type) {
                case OUTPUT -> children.size() < 1;
                case OPERATOR -> children.size() < getMaxChildren();
                default -> false;
            };
        }

        public boolean isChildValid(LogicObject child) {
            // Modules and states can only be children of operators or output
            // Operators can have modules, states, or other operators as children
            return true;
        }

        private int getMaxChildren() {
            // Operators like AND/OR can have 2 children, NOT has 1
            return data == 2 ? 1 : 2; // Assuming 2 = NOT operator
        }

        public boolean canBeRemoved() {
            return type != LogicType.OUTPUT;
        }

        public int[] getRect() { return rect; }
        public void setRect(int[] rect) { this.rect = rect; }

        public String getName() {
            return switch (type) {
                case MODULE -> "Module " + data;
                case STATE -> "State " + data;
                case OPERATOR -> getOperatorName(data);
                case OUTPUT -> "Output";
            };
        }

        private String getOperatorName(byte id) {
            return switch (id) {
                case 0 -> "AND";
                case 1 -> "OR";
                case 2 -> "NOT";
                case 3 -> "XOR";
                default -> "OP" + id;
            };
        }
    }
}

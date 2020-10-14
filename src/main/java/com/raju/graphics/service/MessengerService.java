package com.raju.graphics.service;

import com.raju.constants.ProjectConstants;
import com.raju.graphics.enums.EventType;
import com.raju.graphics.models.Block;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.event.Event;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class MessengerService {

    private static MessengerService messengerService;
    private ShapeService shapeService;

    private MessengerService() {
        this.shapeService = ShapeService.getInstance();
    }

    public static MessengerService getInstance() {
        if (messengerService == null) {
            messengerService = new MessengerService();
        }
        return messengerService;
    }

    public void handleMessageFromSocket(String eventMsg) {
        String[] infoArray = eventMsg.split(ProjectConstants.EVENT_SEPARATOR);
        EventType eventType = EventType.valueOf(infoArray[0]);
        int blockIndex = Integer.parseInt(infoArray[1]);
        boolean isFirstPlayer = shapeService.isIsfirstPlayer();
        Map<Integer, Block> blockMap = shapeService.getBlockMap();
        shapeService.setSocketMsg(eventType.name());
        switch (eventType) {
            case PIECE_SELECT:
                Block selectedBlock = blockMap.get(blockIndex);
                shapeService.setSelectedBlock(selectedBlock);
                fireClickEvent(selectedBlock);
                break;
            case PIECE_MOVE:
                Block attackingBlock = blockMap.get(blockIndex);
                shapeService.setSelectedBlock(attackingBlock);
                Block targetBlock = blockMap.get(Integer.parseInt(infoArray[2]));
                fireClickEvent(targetBlock);
        }
    }

    private void fireClickEvent(Block targetBlock) {
        Group group = targetBlock.getNode();
        int targetBlockNum = targetBlock.getBlockNum();
        int rowNum = targetBlockNum / ProjectConstants.NUM_CELLS_IN_ROW;
        int colNum = targetBlockNum % ProjectConstants.NUM_CELLS_IN_ROW;
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED,
                colNum * ProjectConstants.CELL_SIZE, rowNum * ProjectConstants.CELL_SIZE,
                colNum * ProjectConstants.CELL_SIZE, rowNum * ProjectConstants.CELL_SIZE, MouseButton.PRIMARY, 1,
                true, true, true, true, true, true,
                true, true, true, true, null);
        Event.fireEvent(group, mouseEvent);
    }

}

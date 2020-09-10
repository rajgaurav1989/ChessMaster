package com.raju.constants;

import com.raju.graphics.enums.MovementLimit;
import com.raju.graphics.enums.MovementType;
import com.raju.graphics.enums.PieceType;
import com.raju.graphics.models.PieceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProjectConstants {
    public static final String LOCALHOST_IP_KEY = "127.0.0.1";
    public static final int CELL_SIZE = 80;
    public static final int NUM_CELLS = 8;
    public static final int WINDOW_SIZE = CELL_SIZE * NUM_CELLS;
    public static final String TITLE = "Raj's Chess Master";
    public static final float OPACITY_FACTOR = 0.20f;
    public static final float FULL_OPACITY_FACTOR = 1f ;
    public static final int WHITE_BOARD_ROTATION_ANGLE = 180;

    public static final Map<Integer, String> pathMap = new HashMap<Integer, String>() {
        {
            put(0, "WhiteRook.jpg");
            put(7, "WhiteRook.jpg");
            put(1, "WhiteKnight.jpg");
            put(6, "WhiteKnight.jpg");
            put(2, "WhiteBishop.jpg");
            put(5, "WhiteBishop.jpg");
            put(3, "WhiteQueen.jpg");
            put(4, "WhiteKing.jpg");
            put(8, "WhitePawn.jpg");
            put(9, "WhitePawn.jpg");
            put(10, "WhitePawn.jpg");
            put(11, "WhitePawn.jpg");
            put(12, "WhitePawn.jpg");
            put(13, "WhitePawn.jpg");
            put(14, "WhitePawn.jpg");
            put(15, "WhitePawn.jpg");

            put(63, "BlackRook.jpg");
            put(56, "BlackRook.jpg");
            put(62, "BlackKnight.jpg");
            put(57, "BlackKnight.jpg");
            put(61, "BlackBishop.jpg");
            put(58, "BlackBishop.jpg");
            put(60, "BlackKing.jpg");
            put(59, "BlackQueen.jpg");
            put(48, "BlackPawn.jpg");
            put(49, "BlackPawn.jpg");
            put(50, "BlackPawn.jpg");
            put(51, "BlackPawn.jpg");
            put(52, "BlackPawn.jpg");
            put(53, "BlackPawn.jpg");
            put(54, "BlackPawn.jpg");
            put(55, "BlackPawn.jpg");
        }
    };

    public static Map<Integer, PieceType> initialPieceTypeMap = new HashMap<Integer, PieceType>() {
        {
            put(0, PieceType.ROOK);
            put(7, PieceType.ROOK);
            put(1, PieceType.KNIGHT);
            put(6, PieceType.KNIGHT);
            put(2, PieceType.BISHOP);
            put(5, PieceType.BISHOP);
            put(3, PieceType.QUEEN);
            put(4, PieceType.KING);
            put(8, PieceType.PAWN);
            put(9, PieceType.PAWN);
            put(10, PieceType.PAWN);
            put(11, PieceType.PAWN);
            put(12, PieceType.PAWN);
            put(13, PieceType.PAWN);
            put(14, PieceType.PAWN);
            put(15, PieceType.PAWN);

            put(63, PieceType.ROOK);
            put(56, PieceType.ROOK);
            put(62, PieceType.KNIGHT);
            put(57, PieceType.KNIGHT);
            put(61, PieceType.BISHOP);
            put(58, PieceType.BISHOP);
            put(60, PieceType.KING);
            put(59, PieceType.QUEEN);
            put(48, PieceType.PAWN);
            put(49, PieceType.PAWN);
            put(50, PieceType.PAWN);
            put(51, PieceType.PAWN);
            put(52, PieceType.PAWN);
            put(53, PieceType.PAWN);
            put(54, PieceType.PAWN);
            put(55, PieceType.PAWN);
        }
    };


    public static final Map<PieceType, PieceInfo> pieceInfoMap = new HashMap<PieceType, PieceInfo>() {
        {
            put(PieceType.ROOK, new PieceInfo(new ArrayList<MovementType>() {{
                add(MovementType.LINEAR);
            }}, MovementLimit.UNBOUNDED));
            put(PieceType.KNIGHT, new PieceInfo(new ArrayList<MovementType>() {{
                add(MovementType.KNIGHTISH);
            }}, MovementLimit.SINGLE));
            put(PieceType.BISHOP, new PieceInfo(new ArrayList<MovementType>() {{
                add(MovementType.DIAGONAL);
            }}, MovementLimit.UNBOUNDED));
            put(PieceType.KING, new PieceInfo(new ArrayList<MovementType>() {{
                add(MovementType.LINEAR);
                add(MovementType.DIAGONAL);
            }}, MovementLimit.SINGLE));
            put(PieceType.QUEEN, new PieceInfo(new ArrayList<MovementType>() {{
                add(MovementType.LINEAR);
                add(MovementType.DIAGONAL);
            }}, MovementLimit.UNBOUNDED));
            put(PieceType.PAWN, new PieceInfo(new ArrayList<MovementType>() {{
                add(MovementType.LINEAR);
                add(MovementType.DIAGONAL);
            }}, MovementLimit.SINGLE_FORWARD));
        }
    };

}

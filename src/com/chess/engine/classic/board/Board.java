package com.chess.engine.classic.board;

import com.chess.engine.classic.Alliance;
import com.chess.engine.classic.board.Move.MoveFactory;
import com.chess.engine.classic.pieces.*;
import com.chess.engine.classic.player.BlackPlayer;
import com.chess.engine.classic.player.Player;
import com.chess.engine.classic.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

public class Board {
    static int[] number1 = new int[8];
    static int[] number2 = new int[8];

    public static int[] Rand1(){
        for(int i=0; i<8; i++){
            Random rand = new Random();
            int n = rand.nextInt(5);
            number1[i] = n;
        }
        return number1;
    }

    public static int[] Rand2(){
        for(int i=0; i<8; i++){
            Random rand = new Random();
            int n = rand.nextInt(5);
            number2[i] = n;
        }
        return number2;
    }

    private final Int2ObjectMap<Piece> boardConfig;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;
    private final Move transitionMove;

    private static Board STANDARD_BOARD = createStandardBoardImpl(Rand1(), Rand2());

    private Board(final Builder builder) {
        this.boardConfig = Int2ObjectMaps.unmodifiable(builder.boardConfig);
        this.whitePieces = calculateActivePieces(builder, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(builder, Alliance.BLACK);
        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Move> whiteStandardMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardMoves = calculateLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteStandardMoves, blackStandardMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardMoves, blackStandardMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayerByAlliance(this.whitePlayer, this.blackPlayer);
        this.transitionMove = builder.transitionMove != null ? builder.transitionMove : MoveFactory.getNullMove();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = prettyPrint(this.boardConfig.get(i));
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % 8 == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private static String prettyPrint(final Piece piece) {
        if(piece != null) {
            return piece.getPieceAllegiance().isBlack() ?
                    piece.toString().toLowerCase() : piece.toString();
        }
        return "-";
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Iterable<Piece> getAllPieces() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePieces, this.blackPieces));
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),
                                                               this.blackPlayer.getLegalMoves()));
    }

    public WhitePlayer whitePlayer() {
        return this.whitePlayer;
    }

    public BlackPlayer blackPlayer() {
        return this.blackPlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Piece getPiece(final int coordinate) {
        return this.boardConfig.get(coordinate);
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    public Move getTransitionMove() {
        return this.transitionMove;
    }

    public static Board createStandardBoard() {
        return STANDARD_BOARD;
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        return pieces.stream().flatMap(piece -> piece.calculateLegalMoves(this).stream())
                      .collect(Collectors.toList());
    }

    private static Collection<Piece> calculateActivePieces(final Builder builder,
                                                           final Alliance alliance) {
        return builder.boardConfig.values().stream()
               .filter(piece -> piece.getPieceAllegiance() == alliance)
               .collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf));
    }

    public static class Builder {

        Int2ObjectMap<Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;
        Move transitionMove;

        public Builder() {
            this.boardConfig = new Int2ObjectOpenHashMap<>(33, 1.0f);
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Builder setEnPassantPawn(final Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
            return this;
        }

        public Builder setMoveTransition(final Move transitionMove) {
            this.transitionMove = transitionMove;
            return this;
        }

        public Board build() {
            return new Board(this);
        }

    }

    public static Board createStandardBoardImpl(int[] num1, int[] num2) {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Knight(Alliance.BLACK, 1));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new King(Alliance.BLACK, 3, true, true));
        builder.setPiece(new Queen(Alliance.BLACK, 4));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 7));

        for(int i=0; i<8; i++){
            if(num1[i] == 0){
                builder.setPiece(new Rook(Alliance.BLACK, 8+i));
            }
            else if(num1[i]==1){
                builder.setPiece(new Knight(Alliance.BLACK, 8+i));
            }
            else if(num1[i]==2){
                builder.setPiece(new Bishop(Alliance.BLACK, 8+i));
            }
            else if(num1[i]==3){
                builder.setPiece(new Queen(Alliance.BLACK, 8+i));
            }
            else if(num1[i]==4){
                builder.setPiece(new Pawn(Alliance.BLACK, 8+i));
            }
        }

        // White Layout
        for(int i=0; i<8; i++){
            if(num2[i]==0){
                builder.setPiece(new Rook(Alliance.WHITE, 48+i));
            }
            else if(num2[i]==1){
                builder.setPiece(new Knight(Alliance.WHITE, 48+i));
            }
            else if(num2[i]==2){
                builder.setPiece(new Bishop(Alliance.WHITE, 48+i));
            }
            else if(num2[i]==3){
                builder.setPiece(new Queen(Alliance.WHITE, 48+i));
            }
            else if(num2[i]==4){
                builder.setPiece(new Pawn(Alliance.WHITE, 48+i));
            }
        }

        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Knight(Alliance.WHITE, 57));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new King(Alliance.WHITE, 60, true, true));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        //white to move
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        return builder.build();
    }

}

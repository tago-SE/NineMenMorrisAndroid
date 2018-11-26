package tiago.ninemenmorris.model;

public enum GameState {

    /* @https://en.wikipedia.org/wiki/Nine_men%27s_morris */

    PLACING,
    /*  The game begins with an empty board. The players determine who plays first, then take turns
     *  placing their men one per play on empty points. If a player is able to place three of their
     *  pieces on contiguous points in a straight line, vertically or horizontally, they have
     *  formed a mill and may remove one of their opponent's pieces from the board and the game,
     *  with the caveat that a piece in an opponent's mill can only be removed if no other pieces
     *  are available. After all men have been placed, phase two begins.
     */

    MOVING,
    /*  Players continue to alternate moves, this time moving a man to an adjacent point. A piece
     *  may not "jump" another piece. Players continue to try to form mills and remove their
     *  opponent's pieces as in phase one. A player can "break" a mill by moving one of his pieces
     *  out of an existing mill, then moving it back to form the same mill a second time (or any
     *  number of times), each time removing one of his opponent's men. The act of removing an
     *  opponent's man is sometimes called "pounding" the opponent. When one player has been
     *  reduced to three men, phase three begins.
     */

    FLYING,
    /*  When a player is reduced to three pieces, there is no longer a limitation on that player of
     *  moving to only adjacent points: The player's men may "fly" (or "hop",[4][5] or "jump"[6])
     *  from any point to any vacant point.
     */

    PAUSED,
    /* Neither play is able to interact with the game in this stage.
    */
    DELETING;

}

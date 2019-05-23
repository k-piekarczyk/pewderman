package pewderman;

import org.junit.jupiter.api.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Game game;

    @BeforeEach
    void setUp() {
        GameConfiguration config = new GameConfiguration("data/boardData.txt");
        game = new Game(2);
        game.board.fillBoard(config.getWalls());
    }

    @Test
    void shouldNotMoveIntoTheWall() {
        Point firstPos = new Point(game.players[0].cord);

        game.players[0].moveDirection = Player.MoveDirection.UP;
        game.players[0].faceDirection = Player.MoveDirection.UP;

        game.players[0].move();

        assertEquals(firstPos, game.players[0].cord);
    }

    @Test
    void shouldMoveToFreeSpace() {
        Point firstPos = new Point(game.players[0].cord);
        Point expected = new Point(game.players[0].cord);
        expected.x++;

        game.players[0].moveDirection = Player.MoveDirection.RIGHT;
        game.players[0].faceDirection = Player.MoveDirection.RIGHT;

        game.players[0].move();

        assertNotEquals(firstPos, game.players[0].cord);
        assertEquals(expected, game.players[0].cord);
    }

    @Test
    void shouldDropABombOnCurrentCoordinates() {
        game.players[0].dropBomb();

        assertEquals(game.players[0].cord, game.bombs.get(0).cord);
    }

    @Test
    void shouldBeAliveOnStart() {
        assertTrue(game.players[0].isAlive());
    }

    @Test
    void shouldHaveNoHalosOnStart() {
        assertEquals(0, game.players[0].numberOfHalos());
    }

    @Test
    void shouldHaveHalosWithMoreThanOneLife() {
        game.players[0].addLife();
        assertEquals(1, game.players[0].numberOfHalos());

        game.players[0].addLife();
        assertEquals(2, game.players[0].numberOfHalos());

        game.players[0].addLife();
        assertEquals(3, game.players[0].numberOfHalos());

        game.players[0].addLife();
        assertEquals(4, game.players[0].numberOfHalos());
    }

    @Test
    void shouldHaveTheDefaultNameOnStart() {
        assertEquals("player_1", game.players[0].getName());
        assertEquals("player_2", game.players[1].getName());
    }

    @Test
    void shouldDieWhenDamagedWithOneLife() {
        game.players[0].looseALife();
        assertFalse(game.players[0].isAlive());
    }

    @Test
    void shouldLiveWhenDamagedWithMoreThanOneLife() {
        game.players[0].addLife();
        game.players[0].looseALife();
        assertTrue(game.players[0].isAlive());
    }

    @Test
    void shouldNotBuildAWallWithoutTrumpsBlessing() {
        game.players[0].useTrumpsBlessing();
        assertTrue(game.board.fields[game.players[0].cord.x][game.players[0].cord.y].isEmpty());
    }

    @Test
    void shouldBuildAWallWithTrumpsBlessing() {
        game.players[0].addTrumpsBlessing();
        game.players[0].useTrumpsBlessing();

        assertFalse(game.board.fields[game.players[0].cord.x][game.players[0].cord.y].isEmpty());
        assertTrue(game.board.fields[game.players[0].cord.x][game.players[0].cord.y].isAWall());
    }
}
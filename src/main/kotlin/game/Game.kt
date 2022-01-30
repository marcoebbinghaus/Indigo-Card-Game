package indigo.game

import indigo.player.PlayerScore
import kotlin.system.exitProcess

class Game(var currentTurn: Turn) {
    private val firstTurn = currentTurn
    private var lastWinPlayer: Turn? = null
    private val player1Score = PlayerScore()
    private val player2Score = PlayerScore()
    private var considerMostCardBonus = false
    val gameDeck = Deck()
    val tableDeck = EmptyDeck()

    init {
        gameDeck.shuffle()
    }

    fun putCardsOnTable(i: Int) {
        tableDeck.addCards(gameDeck.removeCards(i))
    }

    fun exit(): Nothing {
            println("Game Over")
            exitProcess(0)
    }

    fun printCurrentCardsAndTopCard() {
        println()
        if (tableDeck.size() > 0) {
            println("${tableDeck.size()} cards on the table, and the top card is ${tableDeck.lastCard()}")
        } else {
            println("No Cards on the table")
        }
    }

    fun processPlayedCard(playedCard: Card) {
        val win = isWin(playedCard)
        tableDeck.addCard(playedCard)
        if (win) {
            println("${if (currentTurn == Turn.PLAYER) "Player" else "Computer"} wins cards")
            lastWinPlayer = currentTurn
            when (currentTurn) {
                Turn.PLAYER -> score(player1Score)
                Turn.COMPUTER -> score(player2Score)
            }
        }
        switchTurn()
    }

    private fun switchTurn() {
        currentTurn = if (currentTurn == Turn.COMPUTER) Turn.PLAYER else Turn.COMPUTER
    }

    private fun score(playerScore: PlayerScore) {
        playerScore.score += tableDeck.cards.count { listOf("A", "10", "J", "Q", "K").contains(it.rank) }
        playerScore.wonCards.addCards(tableDeck.removeCards(tableDeck.size()))
        if (considerMostCardBonus) {
            addMostCardBonus()
        }
        printScore()
    }

    private fun printScore() {
        println("Score: Player ${player1Score.score} - Computer ${player2Score.score}")
        println("Cards: Player ${player1Score.wonCards.size()} - Computer ${player2Score.wonCards.size()}")
    }
    private fun isWin(playedCard: Card): Boolean {
        val lastCardOnTable = tableDeck.lastCard() ?: return false
        return playedCard.validCandidateFor(lastCardOnTable)
    }

    fun finalScore() {
        considerMostCardBonus = true
        if (lastWinPlayer != null) {
            score(if (lastWinPlayer == Turn.PLAYER) player1Score else player2Score)
        } else {
            score(if (firstTurn == Turn.PLAYER) player1Score else player2Score)
        }
        addMostCardBonus()
    }

    private fun addMostCardBonus() {
        if (player1Score.wonCards.size() > player2Score.wonCards.size()) {
            player1Score.score += 3
        } else {
            player2Score.score += 3
        }
    }
}

package indigo

import indigo.game.Deck
import indigo.game.Game
import indigo.game.Turn
import indigo.player.Player
import indigo.player.PlayerAI

fun main() {
    println("Indigo Card Game")
    val playFirst = askPlayFirst()


    val player1 = Player()
    val player2 = PlayerAI()
    val game = Game(if (playFirst) Turn.PLAYER else Turn.COMPUTER)

    player1.takeCardsFrom(6, game.gameDeck)
    player2.takeCardsFrom(6, game.gameDeck)

    game.putCardsOnTable(4)
    println("Initial cards on the table: ${game.tableDeck.cards.joinToString(" ")}")
    do {
        game.printCurrentCardsAndTopCard()
        when(game.currentTurn) {
            Turn.PLAYER -> player1.turn(game)
            Turn.COMPUTER -> player2.turn(game)
        }
    } while (canGoOn(game.currentTurn, player1.handCards, player2.handCards))
    game.printCurrentCardsAndTopCard()
    game.finalScore()
    game.exit()
}

fun canGoOn(currentTurn: Turn, player1Deck: Deck, player2Deck: Deck): Boolean {
    return if (currentTurn == Turn.PLAYER) {
        player1Deck.size() > 0
    } else {
        player2Deck.size() > 0
    }
}

fun askPlayFirst(): Boolean {
    var playFirst: Boolean?
            do {
        println("Play first?")
        val userInput = readLine()!!
        playFirst = if (userInput.lowercase() == "yes") {
            true
        } else if (userInput.lowercase() == "no") {
            false
        } else {
            null
        }
    } while(playFirst == null)
    return playFirst
}



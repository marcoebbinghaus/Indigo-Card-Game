package indigo.player

import exception.NotExistingException
import exception.UserWantsToQuitException
import indigo.game.Card
import indigo.game.Deck
import indigo.game.EmptyDeck
import indigo.game.Game

open class Player {

    val handCards = EmptyDeck()

    fun playCard(cardIndex: Int): Card {
        if (handCards.contains(cardIndex)) {
            return handCards.removeCardAtX(cardIndex)
        }
        throw NotExistingException()
    }

    fun playFirstCard(): Card {
        return playCard(0)
    }

    fun takeCardsFrom(cardCount: Int, deck: Deck) {
        handCards.addCards(deck.removeCards(cardCount))
    }

    private fun printCards() {
        println("Cards in hand: ${displayCardsWithIndices()}")
    }

    private fun displayCardsWithIndices(): String {
        return handCards.cards.withIndex().joinToString(" ") { (it.index + 1).toString() + ")" + it.value }
    }

    fun displayCardsPlain() {
        println(handCards.cards.joinToString(" "))
    }

    private fun remainingCards(): Int {
        return handCards.size()
    }

    open fun turn(game: Game) {
        printCards()
        try {
            val chosenCardIndex = waitForUserDecision()
            game.processPlayedCard(playCard(chosenCardIndex))
            if (remainingCards() == 0 && game.gameDeck.size() > 0) {
                takeCardsFrom(6, game.gameDeck)
            }
        } catch (quitException: UserWantsToQuitException) {
            game.exit()
        }
    }

    private fun waitForUserDecision(): Int {
        var userDecision: Int?
        do {
            println("Choose a card to play (1-${handCards.size()}):")
            val userInput = readLine()!!
            if (userInput == "exit") {
                throw UserWantsToQuitException()
            }
            userDecision = try {
                userInput.toInt()
            } catch (e: NumberFormatException) {
                null
            }
            if (userDecision != null && userDecision !in 1 .. handCards.size()) {
                userDecision = null
            }
        } while (userDecision == null)
        return userDecision - 1
    }
}
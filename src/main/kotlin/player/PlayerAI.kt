package indigo.player

import indigo.game.Card
import indigo.game.Deck
import indigo.game.Game
import java.util.Comparator

class PlayerAI : Player() {
    override fun turn(game: Game) {
        displayCardsPlain()
        val card = examineCardToPlay(game.tableDeck)
        val thrownCard = playCard(handCards.cards.indexOf(card))
        println("Computer plays $thrownCard")
        game.processPlayedCard(thrownCard)
        if (handCards.size() == 0 && game.gameDeck.size() >= 6) {
            takeCardsFrom(6, game.gameDeck)
        }
    }

    private fun examineCardToPlay(tableDeck: Deck): Card {
        val candidateCards = fetchCandidateCards(tableDeck)
        if (candidateCards.size == 1) {
            return candidateCards[0]
        }
        val cardsToPickFrom = if (tableDeck.lastCard() == null || candidateCards.isEmpty()) {
            handCards.cards
        } else {
           candidateCards
        }
        return pickCardFrom(cardsToPickFrom)
    }

    private fun pickCardFrom(cardsToPickFrom: List<Card>): Card {
        return if (multipleCardsWithSameSuit(cardsToPickFrom)) {
            pickCardBySameSuit(cardsToPickFrom)
        } else if (multipleCardsWithSameRank(cardsToPickFrom)) {
            pickCardBySameRank(cardsToPickFrom)
        } else {
            cardsToPickFrom[0]
        }
    }

    private fun pickCardBySameRank(cardsToPickFrom: List<Card>): Card {
        return cardsToPickFrom
            .groupBy { it.rank }
            .mapKeys { (key,value) -> value.size }
            .toSortedMap(Comparator.reverseOrder())
            .entries.iterator().next().value[0]
    }

    private fun pickCardBySameSuit(cardsToPickFrom: List<Card>): Card {
        return cardsToPickFrom
            .groupBy { it.color }
            .mapKeys { (key,value) -> value.size }
            .toSortedMap(Comparator.reverseOrder())
            .entries.iterator().next().value[0]
    }

    private fun multipleCardsWithSameRank(cardsToPickFrom: List<Card>): Boolean {
        return cardsToPickFrom.groupBy { it.rank }.values.count { sameRankCards -> sameRankCards.size > 1 } > 0
    }

    private fun multipleCardsWithSameSuit(cardsToPickFrom: List<Card>): Boolean {
        return cardsToPickFrom.groupBy { it.color }.values.count { sameSuitCards -> sameSuitCards.size > 1 } > 0
    }

    private fun fetchCandidateCards(tableDeck: Deck): List<Card> {
        val candidates = mutableListOf<Card>()
        val topCard = tableDeck.lastCard()
        if (topCard != null) {
            for (handCard in handCards.cards) {
                if (handCard.validCandidateFor(topCard)) {
                    candidates.add(handCard)
                }
            }
        }
        return candidates.toList()
    }
}
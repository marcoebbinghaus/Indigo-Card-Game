package indigo.game

open class Deck {
    var cards: MutableList<Card> = mutableListOf()

    init {
        fill()
    }

    open fun fill() {
        cards = mutableListOf()
        for (color in cardColors.reversed()) {
            for (rank in cardRanks.reversed()) {
                cards.add(Card(rank, color))
            }
        }
    }

    fun contains(index: Int): Boolean {
        return cards.indices.contains(index)
    }

    fun shuffle() {
        cards.shuffle()
    }

    fun size(): Int {
        return cards.size
    }

    fun removeCards(number: Int): MutableList<Card> {
        val removedCards = mutableListOf<Card>()
        for (counter in 0 until number) {
            removedCards.add(cards.removeAt(0))
        }
        return removedCards
    }

    fun lastCard(): Card? {
        if (cards.size == 0) {
            return null
        }
        return cards[cards.size - 1]
    }

    fun addCard(card: Card) {
        cards.add(card)
    }

    fun addCards(addedCards: List<Card>) {
        cards.addAll(addedCards)
    }

    fun removeCardAtX(cardIndex: Int): Card {
        return cards.removeAt(cardIndex)
    }

    companion object {
        val cardRanks = mutableListOf("A","2","3","4","5","6","7","8","9","10","J","Q","K")
        val cardColors = mutableListOf('♦','♥','♠','♣')
    }
}

class EmptyDeck : Deck() {
    override fun fill() {
        cards = mutableListOf()
    }
}
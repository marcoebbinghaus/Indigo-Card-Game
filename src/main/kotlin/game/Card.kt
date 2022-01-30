package indigo.game

class Card(val rank: String, val color: Char) {
    override fun toString(): String {
        return "$rank$color"
    }

    fun validCandidateFor(card: Card): Boolean {
        return rank == card.rank || color == card.color
    }
}
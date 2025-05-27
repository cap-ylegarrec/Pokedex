package utils

import kotlin.math.min

fun String.findNearestStringPosition(strings: List<String>): Int? {
    if (strings.isEmpty()) return null

    return strings.mapIndexed { index, s ->
        index to levenshteinDistance(this, s)
    }.minByOrNull { it.second }?.first
}

private fun levenshteinDistance(lhs: String, rhs: String): Int {
    val lhsLength = lhs.length
    val rhsLength = rhs.length

    val dp = Array(lhsLength + 1) { IntArray(rhsLength + 1) }

    for (i in 0..lhsLength) {
        for (j in 0..rhsLength) {
            dp[i][j] = when {
                i == 0 -> j
                j == 0 -> i
                else -> min(
                    dp[i - 1][j - 1] + if (lhs[i - 1] == rhs[j - 1]) 0 else 1,
                    min(dp[i - 1][j] + 1, dp[i][j - 1] + 1)
                )
            }
        }
    }

    return dp[lhsLength][rhsLength]
}


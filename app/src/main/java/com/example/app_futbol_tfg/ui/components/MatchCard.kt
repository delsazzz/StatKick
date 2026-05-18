package com.example.app_futbol_tfg.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.app_futbol_tfg.ui.ui.theme.CardBackground
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary
import com.example.app_futbol_tfg.ui.ui.theme.TextSecondary
import com.example.app_futbol_tfg.ui.ui.theme.LocalAppColors
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.draw.alpha
import com.example.app_futbol_tfg.R


//  Modelo visual temporal para la maqueta de partidos sugeridos.
data class MatchSuggestionUi(
    val id: Int,
    val homeTeam: String,
    val homeCrestUrl: String?,
    val result: String,
    val awayTeam: String,
    val awayCrestUrl: String?,
    val date: String,
    val season: String,
    val competition: String,
    val logoCompetition: String?,
    val competitionId: Int?
)
// Card reutilizable de un partido sugerido.
//@SuppressLint("NotConstructor")
@Composable
fun MatchCard(
    match: MatchSuggestionUi,
    crestSize: Dp,
    resultSize: TextUnit,
    teamNameSize: TextUnit,
    cardPadding: Dp,
    onOpenMatchDetail: (Int) -> Unit
) {
    val appColors = LocalAppColors.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenMatchDetail(match.id) },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            val backgroundRes = getCompetitionBackground(match.competitionId)

            if (backgroundRes != null) {
                Image(
                    painter = painterResource(id = backgroundRes),
                    contentDescription = null,
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(0.10f),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(cardPadding),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Fila principal del partido
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Equipo local
                    Text(
                        text = match.homeTeam,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = appColors.textPrimary,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = teamNameSize,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Escudo local
                    ApiImage(
                        url = match.homeCrestUrl,
                        contentDescription = match.homeTeam,
                        modifier = Modifier.size(crestSize),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    // Resultado
                    Text(
                        text = match.result,
                        color = PrimaryBlue,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = resultSize,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    // Escudo visitante
                    ApiImage(
                        url = match.awayCrestUrl,
                        contentDescription = match.awayTeam,
                        modifier = Modifier.size(crestSize),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Equipo visitante
                    Text(
                        text = match.awayTeam,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = appColors.textPrimary,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = teamNameSize,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                // Segunda fila con información adicional del partido
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = match.date,
                        color = appColors.textSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    DotSeparator()
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = match.season,
                        color = appColors.textSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    DotSeparator()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = match.competition,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = appColors.textSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    ApiImage(
                        url = match.logoCompetition,
                        contentDescription = "Logo de la competición",
                        modifier = Modifier.size(16.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}
    @Composable
    private fun DotSeparator() {
        val appColors = LocalAppColors.current
        Text(
            text = "•",
            color = appColors.textSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }

    private fun getCompetitionBackground(competitionId: Int?): Int? {
        return when (competitionId) {
            140 -> R.drawable.fondo_laliga
            2 -> R.drawable.fondo_champions_league
            else -> null
        }
    }

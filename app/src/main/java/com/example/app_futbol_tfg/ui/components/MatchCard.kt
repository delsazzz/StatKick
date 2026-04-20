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
    val countryFlagUrl: String?
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenMatchDetail(match.id) },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
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
                        color = TextPrimary,
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
                        color = TextPrimary,
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = match.date,
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    DotSeparator()
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = match.season,
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    DotSeparator()
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = match.competition,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    ApiImage(
                        url = match.countryFlagUrl,
                        contentDescription = "País  de la competición",
                        modifier = Modifier.size(18.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
    // Separador entre texto con forma de punto ·
    @Composable
    private fun DotSeparator() {
        Text(
            text = "•",
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }

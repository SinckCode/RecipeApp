package com.pjasoft.recipeapp.ui.screens.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.pjasoft.recipeapp.domain.dtos.Prompt
import com.pjasoft.recipeapp.domain.dtos.RecipeDTO
import com.pjasoft.recipeapp.domain.utils.hideKeyboard
import com.pjasoft.recipeapp.ui.RecipeTheme
import com.pjasoft.recipeapp.ui.components.CustomOutlinedTextField
import com.pjasoft.recipeapp.ui.screens.HomeScreen.components.GeneratedRecipe
import com.pjasoft.recipeapp.ui.screens.HomeScreen.components.Header
import com.pjasoft.recipeapp.ui.screens.HomeScreen.components.RecipeCard
import com.pjasoft.recipeapp.ui.screens.HomeScreen.components.RecipeRow
import com.pjasoft.recipeapp.ui.screens.HomeScreen.components.Tag
import com.pjasoft.recipeapp.ui.viewmodels.RecipeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    val colors = MaterialTheme.colorScheme
    var prompt by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    // TODO: CHANGE THIS TO VIEW MODEL
    var showSheet by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val viewModel : RecipeViewModel = viewModel()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .safeContentPadding()
            .padding(5.dp)
    ) {

        //HEADER
        item {
            Header()
        }
        //FIN DEL HEADER

        item {
            Text(
                text = "Crea, Cocina, Comparte y Disfruta",
                style = MaterialTheme
                    .typography
                    .headlineMedium
                    .copy(fontWeight = FontWeight.ExtraBold)
            )
            Spacer(modifier = Modifier.height(10.dp))
            CustomOutlinedTextField(
                modifier = Modifier.padding(end = 16.dp),
                value = prompt,
                onValueChange = { prompt = it },
                trailingIcon = Icons.Default.AutoAwesome,
                placeHolder = "Escribe tus ingredientes…",
                onTrailingIconClick = {
                    hideKeyboard(focusManager)
                    viewModel.generateRecipe(
                        prompt = Prompt(
                            ingredients = prompt
                        )
                    )
                    showSheet = true
                    scope.launch {
                        sheetState.partialExpand()
                    }

                }
            )
        }

        // FIN CREA COCINA ETC

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(
                    text = "Tus recetas recientes",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFB08F73),
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(viewModel.recipes) { recipe ->
                        RecipeCard(recipe = recipe)
                    }
                }
            }
        }

        item {
            val tags = listOf(
                "Rápidas (10 min)",
                "Pocas Calorias",
                "Sin horno",
                "Desayunos"
            )
            Text(
                text = "Ideas Rápidas"
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tags){ tag ->
                    Tag(text = tag)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.primary.copy(alpha = 0.1f))
                    .padding(20.dp)
                    .clickable{
                        //GENERAR RECETA ALEATORIA
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¿No sabes que cocinar hoy?"
                    )
                    Text(
                        text = "Genera una receta aleatoria"
                    )
                }
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = colors.primary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

        }



        //TODAS LAS RECETAS

        items(viewModel.recipes) { recipe ->


            RecipeRow(
                recipe = recipe,
                onClick = {

                }
            )
        }






    }
    //MODAL
    if (showSheet){
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = colors.surface,
            sheetState = sheetState,
        ){
            val recipe = RecipeDTO(
                category = "Mexicana",
                imageUrl = "https://images.unsplash.com/photo-1613585435238-5577aa11505f?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w4MTg0MDZ8MHwxfHNlYXJjaHw4fHxUb3N0YWRhc3xlbnwwfHx8fDE3NjA5MTU5NzF8MA&ixlib=rb-4.1.0&q=80&w=1080",
                ingredients = listOf(
                    "ajo",
                    "jamón",
                    "jitomate",
                    "cebolla",
                    "queso",
                    "pan",
                    "aguacate",
                    "aceite de oliva",
                    "sal",
                    "azúcar",
                    "agua",
                    "pimienta negra"
                ),
                instructions = listOf(
                    "Reúne y prepara: pica finamente 1 diente de ajo, pica 1/2 cebolla en cubos pequeños, corta 1 jitomate en cubos pequeños, corta el jamón en tiras o cuadros, ralla o corta el queso en láminas, rebana el pan y corta el aguacate en láminas.",
                    "Precalienta una sartén a fuego medio-alto o el grill del horno. Unta ligeramente las rebanadas de pan con aceite de oliva.",
                    "Tuesta las rebanadas de pan en la sartén o grill hasta que estén doradas y crujientes por ambos lados (2–4 minutos). Si quieres, frota cada rebanada con el diente de ajo partido para aromatizar.",
                    "En la misma sartén, añade 1 cucharada de aceite de oliva y sofríe el ajo picado 30 segundos hasta que desprenda aroma; añade la cebolla y cocina 2–3 minutos hasta que esté translúcida.",
                    "Incorpora el jamón al sartén y saltea 2–3 minutos más hasta que tome un ligero dorado. Ajusta con sal y pimienta al gusto (ten en cuenta que el jamón puede ser salado).",
                    "Mientras se cocina, mezcla el jitomate con una pizca de sal, una pizca pequeña de azúcar y una o dos cucharaditas de agua para suavizar la acidez; deja reposar 1 minuto.",
                    "Monta las tostadas: sobre cada rebanada de pan tostado coloca una capa del salteado de jamón y cebolla, añade encima el jitomate preparado, luego el queso y finalmente las láminas de aguacate.",
                    "Si deseas queso fundido, coloca las tostadas montadas bajo el grill 2–3 minutos hasta que el queso se derrita ligeramente.",
                    "Termina con un chorrito pequeño de aceite de oliva y una última pizca de sal y pimienta al gusto. Sirve inmediatamente."
                ),
                minutes = 20,
                stars = 3,
                title = "Tostadas rústicas de jamón, aguacate y queso",
                prompt = ""
            )
            GeneratedRecipe(
                recipe = recipe
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    RecipeTheme {
        HomeScreen()
    }
}
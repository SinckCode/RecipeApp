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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.pjasoft.recipeapp.data.services.Preferences
import com.pjasoft.recipeapp.domain.dtos.Prompt
import com.pjasoft.recipeapp.domain.dtos.RecipeDTO
import com.pjasoft.recipeapp.domain.utils.hideKeyboard
import com.pjasoft.recipeapp.ui.Components.LoadingOverlay
import com.pjasoft.recipeapp.ui.RecipeTheme
import com.pjasoft.recipeapp.ui.components.CustomOutlinedTextField
import com.pjasoft.recipeapp.ui.screens.HomeScreen.components.GeneratedRecipe
import com.pjasoft.recipeapp.ui.screens.HomeScreen.components.Header
import com.pjasoft.recipeapp.ui.screens.HomeScreen.components.RecipeCard
import com.pjasoft.recipeapp.ui.screens.HomeScreen.components.RecipeRow
import com.pjasoft.recipeapp.ui.screens.HomeScreen.components.Tag
import com.pjasoft.recipeapp.ui.screens.HomeScreenRoute
import com.pjasoft.recipeapp.ui.screens.LoginScreenRoute
import com.pjasoft.recipeapp.ui.screens.MainScreenRoute
import com.pjasoft.recipeapp.ui.viewmodels.RecipeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.reflect.KClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController){
    val colors = MaterialTheme.colorScheme
    var prompt by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    // TODO: CHANGE THIS TO VIEW MODEL

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val viewModel : RecipeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                return RecipeViewModel() as T
            }
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .safeContentPadding()
            .padding(5.dp)
    ) {

        //HEADER
        item {
            Header(
                onLoguot = {
                    // Limpia preferencias de sesión
                    Preferences.saveIsLogged(false)
                    Preferences.saveUserId(0)
                    Preferences.clearSettings()

                    navController.navigate(LoginScreenRoute) {
                        popUpTo(MainScreenRoute) {
                            inclusive = true
                        }
                    }
                }
            )
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
                    scope.launch {
                        sheetState.partialExpand()
                    }

                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        hideKeyboard(focusManager)
                        viewModel.generateRecipe(
                            prompt = Prompt(
                                ingredients = prompt
                            )
                        )
                        scope.launch {
                            sheetState.partialExpand()
                        }
                    }
                )
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
                        RecipeCard(recipe){
                            scope.launch {
                                val recipeDTO = RecipeDTO(
                                    category = recipe.category,
                                    ingredients = recipe.ingredients,
                                    instructions = recipe.instructions,
                                    minutes = recipe.minutes,
                                    prompt = "",
                                    stars = recipe.stars,
                                    title = recipe.title,
                                    imageUrl = recipe.imageUrl ?: ""
                                )
                                viewModel.showModalFromList(
                                    recipe = recipeDTO
                                )
                                sheetState.partialExpand()
                            }
                        }
                    }
                }
            }
        }

        item {
            // etiqueta + prompt asociado
            val quickIdeas = listOf(
                "Rápidas (10 min)" to "pasta, ajo, aceite de oliva, tomate",
                "Pocas Calorías"  to "pechuga de pollo, ensalada, lechuga, pepino",
                "Sin horno"       to "sándwich, jamón, queso, pan, aguacate",
                "Desayunos"       to "huevo, pan, café, leche, fruta"
            )

            Text(
                text = "Ideas Rápidas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(quickIdeas) { (label, ideaPrompt) ->
                    Tag(text = label) {
                        // 1) actualizamos el campo de texto
                        prompt = ideaPrompt

                        // 2) escondemos teclado (por si estaba abierto)
                        hideKeyboard(focusManager)

                        // 3) llamamos a la IA
                        viewModel.generateRecipe(
                            prompt = Prompt(ingredients = ideaPrompt)
                        )

                        // 4) abrimos el BottomSheet
                        scope.launch {
                            sheetState.partialExpand()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


        }



        //TODAS LAS RECETAS

        items(viewModel.recipes) { recipe ->
            RecipeRow(recipe){
                scope.launch {
                    val recipeDTO = RecipeDTO(
                        category = recipe.category,
                        ingredients = recipe.ingredients,
                        instructions = recipe.instructions,
                        minutes = recipe.minutes,
                        prompt = "",
                        stars = recipe.stars,
                        title = recipe.title,
                        imageUrl = recipe.imageUrl ?: ""
                    )
                    viewModel.showModalFromList(
                        recipe = recipeDTO
                    )
                    sheetState.partialExpand()
                }
            }
        }






    }
    //MODAL
    if (viewModel.showSheet){
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideModal() },
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = colors.surface,
            sheetState = sheetState,
        ){

            GeneratedRecipe(
                recipe = viewModel.generatedRecipe,
                isFromHistory = viewModel.isFromHistory,
                onSave = {
                    scope.launch {
                        viewModel.hideModal()
                        sheetState.hide()
                    }
                    viewModel.saveRecipeInDb()
                },
                onClose = {
                    scope.launch {
                        viewModel.hideModal()
                        sheetState.hide()
                    }
                }
            )
        }
    }

    if (viewModel.isLoading){
        LoadingOverlay(colors, "Cocinando…")
    }

}

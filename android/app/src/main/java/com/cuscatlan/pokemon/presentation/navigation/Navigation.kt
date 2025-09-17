package com.cuscatlan.pokemon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.cuscatlan.pokemon.data.model.Trainer
import com.cuscatlan.pokemon.di.DependencyContainer
import com.cuscatlan.pokemon.presentation.screens.OptionalTrainerFormScreen
import com.cuscatlan.pokemon.presentation.screens.PokedexScreen
import com.cuscatlan.pokemon.presentation.screens.PokemonDetailScreen
import com.cuscatlan.pokemon.presentation.screens.PokemonSelectionScreen
import com.cuscatlan.pokemon.presentation.screens.TrainerFormScreen
import com.cuscatlan.pokemon.presentation.screens.TrainerProfileScreen
import com.cuscatlan.pokemon.presentation.viewmodels.OptionalTrainerFormViewModel
import com.cuscatlan.pokemon.presentation.viewmodels.PokemonListViewModel
import com.cuscatlan.pokemon.presentation.viewmodels.TrainerFormViewModel

sealed class Screen(val route: String) {
    object TrainerForm : Screen("trainer_form")
    object OptionalTrainerForm : Screen("optional_trainer_form")
    object PokemonSelection : Screen("pokemon_selection")
    object TrainerProfile : Screen("trainer_profile")
    object Pokedex : Screen("pokedex")
    object PokemonDetail : Screen("pokemon_detail/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "pokemon_detail/$pokemonId"
    }
}

@Composable
fun PokemonTrainerNavigation(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    var currentTrainer by remember { mutableStateOf<Trainer?>(null) }
    
    NavHost(
        navController = navController,
        startDestination = Screen.Pokedex.route,
        modifier = modifier
    ) {
        composable(Screen.TrainerForm.route) {
            val trainerFormViewModel: TrainerFormViewModel = viewModel()
            val trainer by trainerFormViewModel.trainer.collectAsStateWithLifecycle()

            // Update current trainer when form is saved
            if (trainer != null && currentTrainer == null) {
                currentTrainer = trainer
            }

            TrainerFormScreen(
                onNavigateNext = {
                    navController.navigate(Screen.PokemonSelection.route) {
                        launchSingleTop = true
                    }
                },
                viewModel = trainerFormViewModel
            )
        }

        composable(Screen.OptionalTrainerForm.route) {
            val optionalTrainerFormViewModel: OptionalTrainerFormViewModel = viewModel()
            val trainer by optionalTrainerFormViewModel.trainer.collectAsStateWithLifecycle()

            // Update current trainer when form is saved
            if (trainer != null) {
                currentTrainer = trainer
            }

            OptionalTrainerFormScreen(
                onNavigateNext = {
                    navController.navigate(Screen.PokemonSelection.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = optionalTrainerFormViewModel
            )
        }
        
        composable(Screen.PokemonSelection.route) {
            val pokemonViewModel: PokemonListViewModel = viewModel { PokemonListViewModel(DependencyContainer.pokemonRepository) }
            val selectedPokemonData by pokemonViewModel.selectedPokemonData.collectAsStateWithLifecycle()
            
            PokemonSelectionScreen(
                onNavigateNext = {
                    // Update trainer with selected Pokemon
                    currentTrainer?.let { trainer ->
                        currentTrainer = trainer.copy(selectedPokemon = selectedPokemonData)
                    }
                    navController.navigate(Screen.TrainerProfile.route) {
                        popUpTo(Screen.TrainerForm.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = pokemonViewModel
            )
        }
        
        composable(Screen.TrainerProfile.route) {
            currentTrainer?.let { trainer ->
                TrainerProfileScreen(
                    trainer = trainer,
                    onEditProfile = {
                        navController.navigate(Screen.OptionalTrainerForm.route) {
                            launchSingleTop = true
                        }
                    },
                    onEditPokemon = {
                        navController.navigate(Screen.PokemonSelection.route) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateBack = {
                        navController.navigate(Screen.Pokedex.route) {
                            popUpTo(Screen.Pokedex.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        composable(Screen.Pokedex.route) {
            PokedexScreen(
                onPokemonClick = { pokemonId ->
                    navController.navigate(Screen.PokemonDetail.createRoute(pokemonId))
                },
                onProfileClick = {
                    navController.navigate(Screen.OptionalTrainerForm.route)
                }
            )
        }

        composable(
            Screen.PokemonDetail.route,
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 1
            PokemonDetailScreen(
                pokemonId = pokemonId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
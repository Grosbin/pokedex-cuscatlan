package com.cuscatlan.pokemon.presentation.screens

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cuscatlan.pokemon.presentation.utils.DUIValidation
import com.cuscatlan.pokemon.presentation.utils.CarnetValidation
import com.cuscatlan.pokemon.presentation.viewmodels.TrainerFormViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerFormScreen(
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TrainerFormViewModel = viewModel()
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val validationErrors by viewModel.validationErrors.collectAsStateWithLifecycle()
    val isFormValid by viewModel.isFormValid.collectAsStateWithLifecycle()
    val trainer by viewModel.trainer.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        viewModel.updatePhotoUri(uri?.toString())
    }
    
    LaunchedEffect(trainer) {
        if (trainer != null) {
            onNavigateNext()
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Configura perfil de entrenador",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        // Photo Section
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Carga de foto *",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = if (formState.photoUri != null) Color.Green else Color.Gray,
                            shape = CircleShape
                        )
                        .background(Color.LightGray.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (formState.photoUri != null) {
                        AsyncImage(
                            model = formState.photoUri,
                            contentDescription = "Trainer photo",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Add photo",
                            modifier = Modifier.size(80.dp),
                            tint = Color.Gray
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6)
                    )
                ) {
                    Text(if (formState.photoUri != null) "Cambiar foto" else "Seleccionar foto")
                }

                if (validationErrors.containsKey("photo")) {
                    Text(
                        text = validationErrors["photo"] ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        
        // Name Field
        OutlinedTextField(
            value = formState.name,
            onValueChange = viewModel::updateName,
            label = { Text("Ingresar nombre *") },
            isError = validationErrors.containsKey("name"),
            supportingText = validationErrors["name"]?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFF3B82F6),
                unfocusedBorderColor = Color.LightGray
            )
        )

        // Hobby Field
        OutlinedTextField(
            value = formState.hobby,
            onValueChange = viewModel::updateHobby,
            label = { Text("Pasatiempo favorito") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFF3B82F6),
                unfocusedBorderColor = Color.LightGray
            )
        )
        
        // Birth Date Field
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        OutlinedTextField(
            value = formState.birthDate?.format(dateFormatter) ?: "",
            onValueChange = { },
            label = { Text("Fecha de nacimiento *") },
            readOnly = true,
            isError = validationErrors.containsKey("birthDate"),
            supportingText = validationErrors["birthDate"]?.let { { Text(it) } },
            trailingIcon = {
                TextButton(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH)
                        val day = calendar.get(Calendar.DAY_OF_MONTH)

                        DatePickerDialog(
                            context,
                            { _, selectedYear, selectedMonth, selectedDay ->
                                val date = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                                viewModel.updateBirthDate(date)
                            },
                            year, month, day
                        ).show()
                    }
                ) {
                    Text("Seleccionar", color = Color(0xFF3B82F6))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFF3B82F6),
                unfocusedBorderColor = Color.LightGray
            )
        )
        
        // Age Display
        if (formState.birthDate != null) {
            val age = LocalDate.now().year - formState.birthDate!!.year -
                    if (LocalDate.now().dayOfYear < formState.birthDate!!.dayOfYear) 1 else 0
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Edad: $age años",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Identification Number Field (DUI for adults, optional for minors)
        val isAdult = formState.birthDate?.let {
            LocalDate.now().minusYears(18).isAfter(it) ||
            LocalDate.now().minusYears(18).isEqual(it)
        } ?: false

        OutlinedTextField(
            value = formState.identificationNumber,
            onValueChange = { input ->
                if (isAdult) {
                    // Auto-format DUI with dash
                    val formatted = DUIValidation.formatDUI(input)
                    viewModel.updateIdentificationNumber(formatted)
                } else {
                    // For minors, allow alphanumeric input
                    viewModel.updateIdentificationNumber(input)
                }
            },
            label = {
                Text(if (isAdult) "DUI *" else "Carnet de minoridad")
            },
            placeholder = {
                Text(if (isAdult) "12345678-9" else "Opcional")
            },
            isError = validationErrors.containsKey("identificationNumber"),
            supportingText = {
                val error = validationErrors["identificationNumber"]
                if (error != null) {
                    Text(error, color = MaterialTheme.colorScheme.error)
                } else if (isAdult) {
                    Text("Formato: 12345678-9", color = Color.Gray)
                } else {
                    Text("Campo opcional para menores de edad", color = Color.Gray)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isAdult) KeyboardType.Number else KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFF3B82F6),
                unfocusedBorderColor = Color.LightGray
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                viewModel.saveTrainer()
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3B82F6),
                disabledContainerColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Siguiente: Selecciona tu equipo Pokémon",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
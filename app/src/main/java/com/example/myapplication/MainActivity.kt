package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.model.Endereco
import com.example.myapplication.model.EnderecoServiceIF
import com.example.myapplication.model.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val enderecoService = RetrofitClient.retrofit.create(EnderecoServiceIF::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnderecoApp()
        }
    }

    @Composable
    fun EnderecoApp() {
        var cep by remember { mutableStateOf("") }
        var endereco by remember { mutableStateOf<Endereco?>(null) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = cep,
                onValueChange = { cep = it },
                label = { Text("Digite o CEP") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                isError = cep.length != 8 && cep.isNotEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (cep.length == 8) {
                    buscarEndereco(cep) {
                        endereco = it
                        errorMessage = null
                    }
                } else {
                    errorMessage = "CEP inválido. Deve ter 8 dígitos."
                }
            }) {
                Text("Buscar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            } else if (endereco != null) {
                EnderecoInfo(endereco!!)
            }
        }
    }

    @Composable
    fun EnderecoInfo(endereco: Endereco) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text("CEP: ${endereco.cep}")
            Text("Logradouro: ${endereco.logradouro}")
            Text("Complemento: ${endereco.complemento ?: "N/A"}")
            Text("Bairro: ${endereco.bairro}")
            Text("Localidade: ${endereco.localidade}")
            Text("UF: ${endereco.uf}")
            Text("IBGE: ${endereco.ibge}")
            Text("GIA: ${endereco.gia ?: "N/A"}")
            Text("DDD: ${endereco.ddd}")
            Text("SIAFI: ${endereco.siafi}")
        }
    }

    private fun buscarEndereco(cep: String, onResult: (Endereco) -> Unit) {
        lifecycleScope.launch {
            try {
                val result = enderecoService.getEndereco(cep)
                onResult(result)
            } catch (e: Exception) {
                // Handle exception if necessary
            }
        }
    }
}


package com.pjasoft.recipeapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pjasoft.recipeapp.data.KtorfitClient
import com.pjasoft.recipeapp.data.services.Preferences
import com.pjasoft.recipeapp.domain.dtos.Login
import com.pjasoft.recipeapp.domain.dtos.Register
import de.jensklingenberg.ktorfit.Ktorfit
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel(){

    var isLoading by mutableStateOf(false)
        private set

    fun register(
        name : String,
        email : String,
        password : String,
        onResult: (Boolean, String) -> Unit
    ){
        viewModelScope.launch {
            try {
                isLoading = true
                val service = KtorfitClient.createAuthService()
                val register = Register(name, email, password)
                val result = service.register(register)

                if(result.isLogged){
                    Preferences.saveUserId(result.userId)
                    Preferences.saveIsLogged(result.isLogged)
                    onResult(true,result.message)
                } else {
                    onResult(false,result.message)
                }
            } catch (e : Exception){
                onResult(false, "Error al registrarte")
            } finally {
                isLoading = false
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ){
        viewModelScope.launch {
            try {
                isLoading = true
                val service = KtorfitClient.createAuthService()
                val login = Login(email, password)
                val result = service.login(login)
                if (result.isLogged){
                    Preferences.saveUserId(result.userId)
                    Preferences.saveIsLogged(result.isLogged)
                    onResult(true, result.message)
                } else {
                    onResult(false, result.message )
                }
            }
            catch (e :  Exception){
                onResult(false, "Error al loguearse")
            } finally {
                isLoading = false
            }
        }
    }
}
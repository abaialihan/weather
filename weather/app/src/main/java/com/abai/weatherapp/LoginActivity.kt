package com.abai.weatherapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.abai.weatherapp.R.layout.register_window
import com.abai.weatherapp.R.layout.signin_window
import com.abai.weatherapp.data.DataResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.register_window.*
import kotlinx.android.synthetic.main.signin_window.*
import com.abai.weatherapp.data.User as User

class LoginActivity : AppCompatActivity() {

    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var users: DatabaseReference = firebaseDatabase.getReference("users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_register.setOnClickListener { v -> showRegisterWindow() }
        button_sign_in.setOnClickListener { v -> showSignInWindow() }
    }

    fun showRegisterWindow(){

        findViewById<MaterialEditText>(R.id.nameField)
        findViewById<MaterialEditText>(R.id.emailField)
        findViewById<MaterialEditText>(R.id.passwordField)

        //класс который создает всплывающие окна
        var dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Registration")
        dialogBuilder.setMessage("Enter all data for registration")

        //LayoutInflater – это класс, который умеет из содержимого layout-файла создать View-элемент
        var inflater = LayoutInflater.from(this)
        //получаем layout b связываем с View обьектом
        var registerWindow: View = inflater.inflate(register_window, null)
        //передаем в dialog View обьект
        dialogBuilder.setView(registerWindow)

        dialogBuilder.setPositiveButton("To register") { dialog, which ->
            //проверяем на пустоту поле emailField
            val emailFieldText = emailField?.text.toString()
            val nameFieldText = nameField?.text.toString()
            val passwordFieldText = passwordField?.text.toString()

            if (emailFieldText == null || emailFieldText.trim() == ""){
                //если пусто выдаем ошибку с подсказкой
                Snackbar.make(root_layout, "Input email", Snackbar.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            if (nameFieldText == null){
                //если пусто выдаем ошибку с подсказкой
                Snackbar.make(root_layout, "Input name", Snackbar.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            if (passwordFieldText == null){
                //если пусто выдаем ошибку с подсказкой
                Snackbar.make(root_layout, "Input password", Snackbar.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            //регистрация пользователя
            firebaseAuth.createUserWithEmailAndPassword(emailField?.text.toString(), passwordField?.text.toString())
                .addOnSuccessListener{

                    var response = DataResponse<User>()

                    response.data?.nam= nameField?.text.toString()
                    response.data?.email = emailField?.text.toString()
                    response.data?.password = passwordField?.text.toString()


                    users.child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(response)
                        .addOnSuccessListener {
                            Snackbar.make(root_layout, "User registration successfuly", Snackbar.LENGTH_SHORT).show()
                        }.addOnFailureListener{
                            Snackbar.make(root_layout, "We have user with this email", Snackbar.LENGTH_SHORT).show()
                        }
                }
        }

        //закрыть диалоговое окно (register_window)
        dialogBuilder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        dialogBuilder.show()

    }

    fun showSignInWindow(){
        findViewById<MaterialEditText>(R.id.emailFields)
        findViewById<MaterialEditText>(R.id.passwordFields)

        var dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Sign in")
        dialogBuilder.setMessage("Enter data for sign in")

        var inflater = LayoutInflater.from(this)
        var registerWindow: View = inflater.inflate(signin_window, null)
        dialogBuilder.setView(registerWindow)

        dialogBuilder.setPositiveButton("Sign in") { dialog, which ->
            val emailFieldsText = emailFields?.text.toString()
            val passwordFieldsText = emailFields?.text.toString()

            if (emailFieldsText == null){
                Snackbar.make(root_layout, "Input your email", Snackbar.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            if (passwordFieldsText == null){
                Snackbar.make(root_layout, "Input your password", Snackbar.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            firebaseAuth.signInWithEmailAndPassword(emailField?.text.toString(), passwordField?.text.toString())
                .addOnSuccessListener{
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }.addOnFailureListener{
                    Snackbar.make(root_layout, "Authorisation Error", Snackbar.LENGTH_SHORT).show()
                }
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }

        dialogBuilder.show()
    }
}

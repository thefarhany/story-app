package com.example.storyapp.ui.login

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.MainActivity
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        setUpAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageLogin, View.TRANSLATION_X, -30F, 30F).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun setUpView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        supportActionBar?.hide()
    }

    private fun setUpAction() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val email = binding.edtLoginEmail.text.toString()
            val password = binding.edtLoginPassword.text.toString()

            when {
                email.isEmpty() -> {
                    binding.edtLoginEmail.error = getString(R.string.empty_email)
                }

                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.edtLoginEmail.error = getString(R.string.invalid_email)
                }

                password.isEmpty() -> {
                    binding.edtLoginPassword.error = getString(R.string.empty_password)
                }

                password.length < 8 -> {
                    binding.edtLoginPassword.error = getString(R.string.invalid_password)
                }

                else -> loginViewModel.login(email, password)
            }
        }

        loginViewModel.isError.observe(this) { isError ->
            if (isError) {
                loginViewModel.errorMessage.observe(this) { errorMessage ->
                    Toast.makeText(this, errorMessage ?: R.string.auth_failed.toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.auth_success, Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}
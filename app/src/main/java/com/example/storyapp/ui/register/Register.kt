package com.example.storyapp.ui.register

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
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.login.Login

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageRegister, View.TRANSLATION_X, -30F, 30F).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
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

    private fun setupAction() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        binding.btnRegister.setOnClickListener {
            val name = binding.edtRegisterUsername.text.toString()
            val email = binding.edtRegisterEmail.text.toString()
            val password = binding.edtRegisterPassword.text.toString()

            when {
                name.isEmpty() -> {
                    binding.edtRegisterUsername.error = getString(R.string.invalid_username)
                }

                email.isEmpty() -> {
                    binding.edtRegisterEmail.error = getString(R.string.empty_email)
                }

                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.edtRegisterEmail.error = getString(R.string.invalid_email)
                }

                password.isEmpty() -> {
                    binding.edtRegisterPassword.error = getString(R.string.empty_password)
                }

                password.length < 8 -> {
                    binding.edtRegisterPassword.error = getString(R.string.invalid_password)
                }

                else -> {
                    registerViewModel.register(name, email, password)
                }
            }
        }

        registerViewModel.isError.observe(this) { isError ->
            if (isError) {
                registerViewModel.errorMessage.observe(this) { errorMessage ->
                    Toast.makeText(
                        this,
                        errorMessage ?: R.string.register_failed.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show()

                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}
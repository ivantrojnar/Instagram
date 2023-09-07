package hr.itrojnar.instagram

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import hr.itrojnar.instagram.app.DarkThemeColorsProvider
import hr.itrojnar.instagram.sign_in.GoogleSignInState
import hr.itrojnar.instagram.view.auth.AuthenticationScreen
import hr.itrojnar.instagram.view.auth.LogInScreen
import hr.itrojnar.instagram.view.auth.LogInState
import hr.itrojnar.instagram.view.auth.SignUpState
import org.junit.Rule
import org.junit.Test

class AuthenticationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val themeColorsProvider = DarkThemeColorsProvider()

    @Test
    fun logInScreen_buttonIsEnabled() {
        val dummyGoogleSignInState = GoogleSignInState()

        val dummyLogInState = LogInState(
            email = "test@test.com",
            password = "Test123#*",
            isEmailValid = true,
            isPasswordValid = true
        )

        composeTestRule.setContent {

            val darkTheme = isSystemInDarkTheme()
            val themeColors = themeColorsProvider.getThemeColors(darkTheme)

            LogInScreen(
                logInState = dummyLogInState,
                themeColors = themeColors,
                onEmailChanged = {},
                onPasswordChanged = {},
                googleSignInState = dummyGoogleSignInState,
                onSignInClick = {},
                onGithubSignIn = {},
                onSignUpClick = {},
                onForgotPasswordClick = {},
                onLogin = {}
            )
        }

        composeTestRule.onNodeWithTag("Email").performTextInput("test@test.com")
        composeTestRule.onNodeWithTag("Password").performTextInput("Test123#*")

        composeTestRule.onNodeWithTag("Log in").assertIsEnabled()
    }

    @Test
    fun logInScreen_buttonIsDisabled() {
        val dummyGoogleSignInState = GoogleSignInState()

        val dummyLogInState = LogInState(
            email = "test@test.com",
            password = "Test123",
            isEmailValid = false,
            isPasswordValid = false
        )

        composeTestRule.setContent {

            val darkTheme = isSystemInDarkTheme()
            val themeColors = themeColorsProvider.getThemeColors(darkTheme)

            LogInScreen(
                logInState = dummyLogInState,
                themeColors = themeColors,
                onEmailChanged = {},
                onPasswordChanged = {},
                googleSignInState = dummyGoogleSignInState,
                onSignInClick = {},
                onGithubSignIn = {},
                onSignUpClick = {},
                onForgotPasswordClick = {},
                onLogin = {}
            )
        }

        composeTestRule.onNodeWithTag("Email").performTextInput("test@test.com")
        composeTestRule.onNodeWithTag("Password").performTextInput("Test123")

        composeTestRule.onNodeWithTag("Log in").assertIsNotEnabled()
    }

    @Test
    fun authenticationScreen_transitionsToSignUpScreen() {
        val dummyGoogleSignInState = GoogleSignInState()
        val dummyLogInState = LogInState()
        val dummySignUpState = SignUpState()

        composeTestRule.setContent {
            AuthenticationScreen(
                googleSignInState = dummyGoogleSignInState,
                onSignInClick = {},
                logInState = dummyLogInState,
                onLogin = {},
                signUpState = dummySignUpState,
                onSignUp = {},
                onGithubSignIn = {},
                onRequestEmailForForgottenPassword = {},
                onLoginEmailChanged = {},
                onLoginPasswordChanged = {},
                onSignUpEmailChanged = {},
                onSignUpPasswordChanged = {},
                onFullNameChanged = {},
                onImageUriChanged = {},
                resetSignUpState = {}
            )
        }

        composeTestRule.onNodeWithTag("Sign up").performClick()

        composeTestRule.onNodeWithTag("Sign up").assertIsDisplayed()
    }

    @Test
    fun authenticationScreen_transitionsToForgotPasswordScreen() {
        val dummyGoogleSignInState = GoogleSignInState()
        val dummyLogInState = LogInState()
        val dummySignUpState = SignUpState()

        composeTestRule.setContent {
            AuthenticationScreen(
                googleSignInState = dummyGoogleSignInState,
                onSignInClick = {},
                logInState = dummyLogInState,
                onLogin = {},
                signUpState = dummySignUpState,
                onSignUp = {},
                onGithubSignIn = {},
                onRequestEmailForForgottenPassword = {},
                onLoginEmailChanged = {},
                onLoginPasswordChanged = {},
                onSignUpEmailChanged = {},
                onSignUpPasswordChanged = {},
                onFullNameChanged = {},
                onImageUriChanged = {},
                resetSignUpState = {}
            )
        }

        composeTestRule.onNodeWithTag("Forgot password?").performClick()

        composeTestRule.onNodeWithTag("Trouble loggin in?").assertIsDisplayed()
    }
}
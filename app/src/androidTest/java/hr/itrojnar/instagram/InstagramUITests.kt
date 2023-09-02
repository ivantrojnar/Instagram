package hr.itrojnar.instagram

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import hr.itrojnar.instagram.sign_in.GoogleSignInState
import hr.itrojnar.instagram.view.auth.LogInScreen
import hr.itrojnar.instagram.view.auth.LogInState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstagramUITests {


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun logInScreen_displaysGoogleSignInButton() {
        val dummyGoogleSignInState = GoogleSignInState()  // This might require more setup depending on its constructor and dependencies
        val dummyLogInState = LogInState()  // Similarly, this might require setup

        composeTestRule.setContent {
            LogInScreen(
                logInState = dummyLogInState,
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

        composeTestRule.onNodeWithContentDescription("Google Icon").assertIsDisplayed()
    }

    @Test
    fun logInScreen_displaysGithubIcon() {
        val dummyGoogleSignInState = GoogleSignInState()
        val dummyLogInState = LogInState()

        composeTestRule.setContent {
            LogInScreen(
                logInState = dummyLogInState,
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

        composeTestRule.onNodeWithContentDescription("GitHub Icon").assertIsDisplayed()
    }

    @Test
    fun logInScreen_displaysEmailField() {
        val dummyGoogleSignInState = GoogleSignInState()
        val dummyLogInState = LogInState()

        composeTestRule.setContent {
            LogInScreen(
                logInState = dummyLogInState,
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

        composeTestRule.onNodeWithTag("Email").assertIsDisplayed()
    }
}
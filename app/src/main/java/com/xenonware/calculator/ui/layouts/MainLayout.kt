package com.xenonware.calculator.ui.layouts

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntSize
import com.xenonware.calculator.ui.res.buttons.CoverButtonLayout
import com.xenonware.calculator.viewmodel.CalculatorViewModel
import com.xenonware.calculator.viewmodel.LayoutType
import com.xenonware.todolist.presentation.sign_in.SignInViewModel
import com.xenonware.todolist.ui.layouts.todo.CompactTodo
import com.xenonware.todolist.ui.layouts.todo.CoverTodo
import com.xenonware.todolist.viewmodel.LayoutType
import com.xenonware.todolist.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainLayout(
    viewModel: CalculatorViewModel,
    isLandscape: Boolean,
    layoutType: LayoutType,
    onOpenSettings: () -> Unit,
    onOpenConverter: () -> Unit,
    appSize: IntSize,
) {

    when (layoutType) {
        LayoutType.COVER -> {
            if (isLandscape) {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    appSize = appSize
                )
            } else {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    appSize = appSize
                )
            }
        }

        LayoutType.SMALL -> {
            if (isLandscape) {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    appSize = appSize
                )
            } else {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    appSize = appSize
                )
            }
        }

        LayoutType.COMPACT -> {
            if (isLandscape) {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    appSize = appSize
                )
            } else {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    appSize = appSize
                )
            }
        }

        LayoutType.MEDIUM -> {
            if (isLandscape) {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    appSize = appSize
                )
            } else {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    appSize = appSize
                )
            }
        }

        LayoutType.EXPANDED -> {
            if (isLandscape) {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = true,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    appSize = appSize
                )
            } else {
                CompactCalculator(
                    viewModel = viewModel,
                    isLandscape = false,
                    layoutType = layoutType,
                    onOpenSettings = onOpenSettings,
                    appSize = appSize
                )
            }
        }
    }
}

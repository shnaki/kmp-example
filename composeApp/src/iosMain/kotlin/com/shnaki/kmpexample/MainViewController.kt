package com.shnaki.kmpexample

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Called from Swift/Objective-C as the root UIViewController for the app.
 *
 * In the Xcode project, reference this from ContentView.swift:
 *   struct ComposeView: UIViewControllerRepresentable {
 *       func makeUIViewController(context: Context) -> some UIViewController {
 *           MainViewControllerKt.MainViewController()
 *       }
 *       func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {}
 *   }
 */
fun MainViewController() = ComposeUIViewController { App() }

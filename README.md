# kmp-example

Kotlin Multiplatform + Compose Multiplatform のお試しリポジトリ。  
**単一の Kotlin コード**で Desktop / Web / Android / iOS の UI を共有します。

## アーキテクチャ

```
composeApp/src/
├── commonMain/     ← 全プラットフォーム共通 (App.kt, Platform.kt expect, Greeting.kt)
├── desktopMain/    ← JVM Desktop actual + entry point (Window)
├── wasmJsMain/     ← Kotlin/Wasm actual + entry point (CanvasBasedWindow)
├── androidMain/    ← Android actual + MainActivity
└── iosMain/        ← iOS actual + MainViewController (Kotlin/Native)
```

KMP のコア概念 `expect / actual`:
- `commonMain/Platform.kt` に **`expect fun getPlatform(): Platform`** を宣言
- 各 `*Main` で **`actual fun getPlatform()`** を実装し、プラットフォーム名を返す
- `App.kt` の UI はすべてのプラットフォームで **そのまま動く**

## バージョン

| ライブラリ | バージョン |
|---|---|
| Kotlin | 2.2.20 |
| Compose Multiplatform | 1.11.0 |
| Gradle | 8.11.1 |
| Android Gradle Plugin | 8.8.0 |

## 実行方法

### 前提

- JDK 17 以上（このリポジトリは Java 25 で動作確認済み）
- インターネット接続（初回は Gradle + 依存関係のダウンロードに数分かかります）

---

### Desktop（Windows / macOS / Linux で今すぐ動く）

```bash
./gradlew :composeApp:run
```

ウィンドウが開き、ボタンを押すとカウントアップ＋「Desktop · Java 25」と表示されます。

---

### Web — Kotlin/Wasm（ブラウザで動く）

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

ブラウザが自動で開き、Compose UI が表示されます。

本番ビルド:
```bash
./gradlew :composeApp:wasmJsBrowserProductionWebpack
# → composeApp/build/dist/wasmJs/productionExecutable/ に出力
```

---

### Android（Android SDK が必要）

1. **Android Studio** をインストール（SDK も一緒にインストールされます）
2. `local.properties` に SDK パスを追加:
   ```properties
   sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
   ```
3. `gradle.properties` を更新:
   ```properties
   kmp.android.enabled=true
   ```
4. ビルド & 実行:
   ```bash
   ./gradlew :composeApp:assembleDebug
   # またはエミュレータ/実機に直接インストール:
   ./gradlew :composeApp:installDebug
   ```

---

### iOS（macOS + Xcode が必要）

> **Windows では iOS ビルドは不可**です。macOS 環境で作業してください。

1. macOS で Xcode をインストール
2. [KMP ウィザード](https://kmp.jetbrains.com/) で iOS 向け Xcode プロジェクトを生成し、  
   `iosApp/` として追加（このリポジトリでは省略）
3. または Xcode から直接ビルド:
   ```bash
   ./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
   ```

## プロジェクト構造

```
kmp-example/
├── settings.gradle.kts         # プロジェクト設定
├── build.gradle.kts            # ルート（プラグイン宣言のみ）
├── gradle.properties           # Gradle フラグ / kmp.android.enabled
├── local.properties            # Android SDK パス（git 管理外）
├── gradle/
│   ├── libs.versions.toml      # バージョンカタログ
│   └── wrapper/                # Gradle Wrapper
└── composeApp/
    ├── build.gradle.kts        # KMP / CMP ターゲット設定
    └── src/
        ├── commonMain/         # 共通コード
        ├── desktopMain/        # Desktop 専用
        ├── wasmJsMain/         # Web 専用
        ├── androidMain/        # Android 専用
        └── iosMain/            # iOS 専用
```

## よく使うタスク

| タスク | 説明 |
|---|---|
| `./gradlew tasks` | 利用可能なタスク一覧 |
| `./gradlew :composeApp:run` | Desktop アプリ起動 |
| `./gradlew :composeApp:wasmJsBrowserDevelopmentRun` | ブラウザで Web 起動 |
| `./gradlew :composeApp:assembleDebug` | Android APK ビルド（SDK 必要） |
| `./gradlew build` | 全ターゲットビルド |

# RUIN CLIENT — source starter v0.1.0

Minecraft Java **1.21.11**, Fabric, Java 21.

## Included
- Right Shift opens a dark/purple module directory.
- Search, category switching, module state toggles and scroll.
- Eight theme accents with JSON persistence at `config/ruin-client.json`.
- A custom Ruin landing screen accessible through **HOME** in the GUI.
- Registrations for the requested Crystal PvP, UHC, Mace PvP, SMP, interface and BedWars categories.

## Important limitations
- **The combat, movement, and bridging modules are registered UI placeholders, not implemented gameplay cheats.** Toggling them only changes their saved enabled state.
- The custom landing screen is opened from the GUI, **not yet a replacement for Minecraft's default title screen**.
- This source has **not been compiled or game-tested** in this environment. Check dependency versions against Fabric's published 1.21.11 artifacts, then fix any mappings/API differences.
- No executable JAR or Gradle wrapper is included.
- The visual layout is a functional minimal scaffold, **not a pixel-perfect reproduction of the reference**.

## Build
1. Install Java 21 and Gradle 8.14+ compatible with your chosen Fabric Loom release.
2. Check `gradle.properties` and `build.gradle` against current Fabric 1.21.11 versions.
3. Run `gradle build` in the project root.
4. If successful, find the compiled mod in `build/libs/` and install it with the matching Fabric Loader and Fabric API.

## Structure
- `RuinClient.java` — entrypoint, categories, config and theme palette.
- `RuinScreen.java` — interactive ClickGUI.
- `RuinHomeScreen.java` — branded landing screen.

## Next implementation phases
1. Verify build, API and Minecraft integration.
2. Add a true custom title screen and UI rendering animations.
3. Implement safe private-world bridge practice, HUD, and input visualization.
4. Implement individual modules with settings, tests, and clear supported-server constraints.

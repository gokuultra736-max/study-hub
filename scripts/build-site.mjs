// Copies the static landing page into dist/ and adds the app logo from the Android resources.
import { cpSync, mkdirSync, rmSync } from "node:fs";

rmSync("dist", { recursive: true, force: true });
mkdirSync("dist", { recursive: true });
cpSync("web", "dist", { recursive: true });
cpSync("app/src/main/res/drawable/ic_study_logo_1794494366844.jpg", "dist/logo.jpg");
console.log("Landing page built into dist/");

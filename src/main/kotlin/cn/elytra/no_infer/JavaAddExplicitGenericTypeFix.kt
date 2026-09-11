package cn.elytra.no_infer

import com.intellij.codeInsight.daemon.impl.quickfix.AddTypeArgumentsFix
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.modcommand.ModPsiUpdater
import com.intellij.modcommand.PsiUpdateModCommandQuickFix
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethodCallExpression

class JavaAddExplicitGenericTypeFix : PsiUpdateModCommandQuickFix() {
    override fun getFamilyName(): @IntentionFamilyName String = NoInferMessageBundle.message("no_infer.quickfix")

    // credits to "com.intellij.codeInspection.compiler.JavacQuirksInspectionVisitor.MyAddExplicitTypeArgumentsFix"
    override fun applyFix(
        project: Project,
        element: PsiElement,
        updater: ModPsiUpdater,
    ) {
        if (element is PsiMethodCallExpression) {
            val withArgs = AddTypeArgumentsFix.addTypeArguments(element, null) ?: return
            element.replace(withArgs)
        }
    }
}

package cn.elytra.no_infer

import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.modcommand.ModPsiUpdater
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.idea.codeinsight.api.applicable.inspections.KotlinModCommandQuickFix
import org.jetbrains.kotlin.idea.codeinsight.utils.addTypeArguments
import org.jetbrains.kotlin.psi.KtCallExpression

class KotlinAddExplicitGenericTypeFix(
    private val typeArguments: String,
) : KotlinModCommandQuickFix<KtCallExpression>() {
    override fun getFamilyName(): @IntentionFamilyName String = NoInferMessageBundle.message("no_infer.quickfix")

    override fun applyFix(
        project: Project,
        element: KtCallExpression,
        updater: ModPsiUpdater,
    ) {
        // the type arguments were rendered when the problem was created, so the call may have been changed since then.
        if (element.typeArguments.isNotEmpty()) {
            return
        }
        addTypeArguments(element, typeArguments, project)
    }
}

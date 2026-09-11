package cn.elytra.no_infer

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtTypeArgumentList

private const val REMOVE_EXPLICIT_TYPE_ARGUMENTS = "RemoveExplicitTypeArguments"

class KotlinRemoveExplicitTypeArgumentsSuppressor : InspectionSuppressor {
    override fun isSuppressedFor(
        element: PsiElement,
        toolId: String,
    ): Boolean {
        if (toolId != REMOVE_EXPLICIT_TYPE_ARGUMENTS) {
            return false
        }
        val call = element.findEnclosingCallExpression() ?: return false
        return call.isValid && analyze(call) { resolveNoInferFunction(call) != null }
    }

    override fun getSuppressActions(
        element: PsiElement?,
        toolId: String,
    ): Array<out SuppressQuickFix?> = SuppressQuickFix.EMPTY_ARRAY
}

private fun PsiElement.findEnclosingCallExpression(): KtCallExpression? {
    var current: PsiElement? = this
    while (current != null) {
        when (current) {
            is KtTypeArgumentList -> return current.parent as? KtCallExpression
            is KtCallExpression -> return current
            else -> current = current.parent
        }
    }
    return null
}

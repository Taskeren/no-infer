package cn.elytra.no_infer

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethodCallExpression

class JavaRedundantTypeArgumentsSuppressor : InspectionSuppressor {
    override fun isSuppressedFor(
        element: PsiElement,
        toolId: String,
    ): Boolean {
        if (toolId == "RedundantTypeArguments") {
            val call = findEnclosingMethodCall(element)
            if (call != null) {
                val method = call.resolveMethod()
                return method != null && isAnnotatedWithExplicitGeneric(method)
            }
        }
        return false
    }

    private fun findEnclosingMethodCall(element: PsiElement): PsiMethodCallExpression? {
        var element: PsiElement? = element
        while (element != null) {
            if (element is PsiMethodCallExpression) {
                return element
            }
            element = element.parent
        }
        return null
    }

    override fun getSuppressActions(
        element: PsiElement?,
        toolId: String,
    ): Array<out SuppressQuickFix?> = SuppressQuickFix.EMPTY_ARRAY
}

import android.content.Context
import android.content.SharedPreferences

class BalanceManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

    // Função para obter o saldo
    fun getBalance(): Float {
        return sharedPreferences.getFloat("balance", 0.0f)
    }

    // Função para verificar se o saldo é negativo
    fun isBalanceNegative(): Boolean {
        return getBalance() < 0
    }

    // Função para salvar um novo saldo
    fun setBalance(value: Float) {
        sharedPreferences.edit().putFloat("balance", value).apply()
    }
}

package com.example.kaouzaystyle.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.example.kaouzaystyle.ui.login.LoginActivity
import com.example.kaouzaystyle.util.NotificationHelper

class AutoLogoutService : Service() {

    private val handler = Handler(Looper.getMainLooper())

    // --- TEMPS AVANT DÉCONNEXION ---
    private val TIMEOUT_TIME = 5 * 60 * 1000L

    private val logoutRunnable = Runnable {
        performLogout()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Cette méthode est appelée à chaque fois qu'on fait startService()
        // Donc, à chaque clic utilisateur, on passe ici et on RESET le timer.
        resetTimer()
        return START_STICKY
    }

    private fun resetTimer() {
        Log.d("AutoLogoutService", "Timer réinitialisé (L'utilisateur est actif)")
        // 1. Annuler le précédent compte à rebours
        handler.removeCallbacks(logoutRunnable)
        // 2. Lancer un nouveau compte à rebours
        handler.postDelayed(logoutRunnable, TIMEOUT_TIME)
    }

    private fun performLogout() {
        Log.d("AutoLogoutService", "Temps écoulé ! Déconnexion en cours...")

        // 1. Envoyer la Notification (Obligatoire selon le prof)
        NotificationHelper.sendLogoutNotification(this)

        // 2. Vider les SharedPreferences (Oublier l'utilisateur)
        val sharedPref = getSharedPreferences("UserProfile", MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // 3. Ouvrir l'écran de Login
        val intent = Intent(this, LoginActivity::class.java)
        // Ces flags sont importants pour fermer toutes les pages précédentes
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

        // 4. Arrêter ce service
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Nettoyage pour éviter les fuites de mémoire
        handler.removeCallbacks(logoutRunnable)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // Ce n'est pas un service lié
    }
}
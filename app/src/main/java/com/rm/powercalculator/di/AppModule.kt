package com.rm.powercalculator.di

import android.app.Application
import androidx.room.Room
import com.rm.powercalculator.data.local.CalculationDao
import com.rm.powercalculator.data.local.CalculationDatabase
import com.rm.powercalculator.data.repository.CalculationRepositoryImpl
import com.rm.powercalculator.domain.repository.CalculationRepository
import com.rm.powercalculator.domain.use_case.Calculate1RMUseCase
import com.rm.powercalculator.domain.use_case.DeleteHistoryUseCase
import com.rm.powercalculator.domain.use_case.GetHistoryUseCase
import com.rm.powercalculator.domain.use_case.SaveHistoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCalculationDatabase(app: Application): CalculationDatabase {
        return Room.databaseBuilder(
            app,
            CalculationDatabase::class.java,
            "calculation_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideCalculationDao(db: CalculationDatabase): CalculationDao {
        return db.calculationDao()
    }

    @Provides
    @Singleton
    fun provideCalculationRepository(dao: CalculationDao): CalculationRepository {
        return CalculationRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideCalculate1RMUseCase(): Calculate1RMUseCase {
        return Calculate1RMUseCase()
    }

    @Provides
    @Singleton
    fun provideGetHistoryUseCase(repository: CalculationRepository): GetHistoryUseCase {
        return GetHistoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveHistoryUseCase(repository: CalculationRepository): SaveHistoryUseCase {
        return SaveHistoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteHistoryUseCase(repository: CalculationRepository): DeleteHistoryUseCase {
        return DeleteHistoryUseCase(repository)
    }
}

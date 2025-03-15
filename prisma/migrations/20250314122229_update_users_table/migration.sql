/*
  Warnings:

  - You are about to drop the column `birthdate` on the `users` table. All the data in the column will be lost.
  - Added the required column `birthDate` to the `users` table without a default value. This is not possible if the table is not empty.
  - Added the required column `birthMonth` to the `users` table without a default value. This is not possible if the table is not empty.
  - Added the required column `birthYear` to the `users` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE `users` DROP COLUMN `birthdate`,
    ADD COLUMN `birthDate` INTEGER NOT NULL,
    ADD COLUMN `birthMonth` INTEGER NOT NULL,
    ADD COLUMN `birthYear` INTEGER NOT NULL;
